package com.example.chatter.presentation.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.data.local.ModelDao
import com.example.chatter.data.local.ModelEntity
import com.example.chatter.domain.MlcEngineWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModelManagerViewModel @Inject constructor(
    private val modelDao: ModelDao,
    private val engine: MlcEngineWrapper
) : ViewModel() {

    val downloadedModels: StateFlow<List<ModelEntity>> = modelDao.getAllModels()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _uiState = MutableStateFlow(ManagerUiState(memoryStatus = engine.getMemoryStatus()))
    val uiState: StateFlow<ManagerUiState> = _uiState.asStateFlow()

    fun loadModel(modelPath: String, modelId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, loadingModelId = modelId)
            engine.loadModel(modelPath)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                loadingModelId = null,
                loadedModelId = modelId,
                memoryStatus = engine.getMemoryStatus()
            )
        }
    }

    fun unloadModel() {
        viewModelScope.launch {
            engine.unloadModel()
            _uiState.value = _uiState.value.copy(
                loadedModelId = null,
                memoryStatus = engine.getMemoryStatus()
            )
        }
    }

    fun deleteModel(model: ModelEntity) {
        viewModelScope.launch {
            if (_uiState.value.loadedModelId == model.id) {
                unloadModel()
            }
            java.io.File(model.localPath).deleteRecursively()
            modelDao.deleteModel(model.id)
        }
    }
}

data class ManagerUiState(
    val isLoading: Boolean = false,
    val loadingModelId: String? = null,
    val loadedModelId: String? = null,
    val memoryStatus: String = ""
)

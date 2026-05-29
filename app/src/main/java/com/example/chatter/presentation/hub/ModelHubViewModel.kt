package com.example.chatter.presentation.hub

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.data.local.ModelDao
import com.example.chatter.data.local.ModelEntity
import com.example.chatter.data.network.ModelDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import android.widget.Toast
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ModelHubViewModel @Inject constructor(
    private val modelDao: ModelDao,
    private val modelDownloader: ModelDownloader,
    private val application: Application
) : ViewModel() {

    private val availableModels = listOf(
        ModelEntity("mlc-ai/gemma-2-2b-it-q4f16_1-MLC", "Gemma-2-2B (4-bit)", 1500L * 1024 * 1024, "", false),
        ModelEntity("mlc-ai/Llama-3.2-3B-Instruct-q4f16_0-MLC", "Llama-3.2-3B (4-bit)", 1500L * 1024 * 1024, "", false),
        ModelEntity("mlc-ai/Llama-3-8B-Instruct-q4f16_1-MLC", "Llama-3 8B (4-bit)", 4500L * 1024 * 1024, "", false)
    )

    private val _uiState = MutableStateFlow(HubUiState(models = availableModels))
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            modelDao.getAllModels().collect { downloadedModels ->
                val downloadedIds = downloadedModels.map { it.id }.toSet()
                val updatedModels = availableModels.map { 
                    it.copy(isDownloaded = downloadedIds.contains(it.id)) 
                }
                _uiState.value = _uiState.value.copy(models = updatedModels)
            }
        }
    }

    fun downloadModel(model: ModelEntity) {
        viewModelScope.launch {
            val currentDownloads = _uiState.value.downloadProgress.toMutableMap()
            currentDownloads[model.id] = 0
            _uiState.value = _uiState.value.copy(downloadProgress = currentDownloads)

            try {
                val destDir = File(application.filesDir, model.id.replace("/", "_"))
                
                modelDownloader.downloadHuggingFaceModel(model.id, destDir).collect { progress ->
                    val updatedDownloads = _uiState.value.downloadProgress.toMutableMap()
                    updatedDownloads[model.id] = progress
                    _uiState.value = _uiState.value.copy(downloadProgress = updatedDownloads)
                }

                val downloadedModel = model.copy(isDownloaded = true, localPath = destDir.absolutePath)
                modelDao.insertModel(downloadedModel)

                val finalDownloads = _uiState.value.downloadProgress.toMutableMap()
                finalDownloads.remove(model.id)
                _uiState.value = _uiState.value.copy(downloadProgress = finalDownloads)
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(application, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
                val errorDownloads = _uiState.value.downloadProgress.toMutableMap()
                errorDownloads.remove(model.id)
                _uiState.value = _uiState.value.copy(downloadProgress = errorDownloads)
            }
        }
    }
}

data class HubUiState(
    val models: List<ModelEntity> = emptyList(),
    val downloadProgress: Map<String, Int> = emptyMap()
)

package com.example.chatter.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.domain.MlcEngineWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val engine: MlcEngineWrapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(prompt: String) {
        if (prompt.isBlank()) return

        val userMessage = ChatMessage(role = "user", content = prompt)
        val initialMessages = _uiState.value.messages + userMessage
        _uiState.value = _uiState.value.copy(
            messages = initialMessages,
            isGenerating = true
        )

        val aiMessageIndex = initialMessages.size
        _uiState.value = _uiState.value.copy(
            messages = initialMessages + ChatMessage(role = "ai", content = "")
        )

        viewModelScope.launch {
            try {
                engine.generateStream(prompt).collect { token ->
                    val currentMessages = _uiState.value.messages.toMutableList()
                    val aiMsg = currentMessages[aiMessageIndex]
                    currentMessages[aiMessageIndex] = aiMsg.copy(content = aiMsg.content + token)
                    _uiState.value = _uiState.value.copy(messages = currentMessages)
                }
            } catch (e: Exception) {
                val currentMessages = _uiState.value.messages.toMutableList()
                val aiMsg = currentMessages[aiMessageIndex]
                currentMessages[aiMessageIndex] = aiMsg.copy(content = aiMsg.content + "\n[Error: ${e.message}]")
                _uiState.value = _uiState.value.copy(messages = currentMessages)
            } finally {
                _uiState.value = _uiState.value.copy(isGenerating = false)
            }
        }
    }
}

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isGenerating: Boolean = false
)

data class ChatMessage(
    val role: String,
    val content: String
)

package com.example.chatter.data

import ai.mlc.mlcllm.MLCEngine
import ai.mlc.mlcllm.OpenAIProtocol.ChatCompletionMessage
import com.example.chatter.domain.MlcEngineWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealMlcEngineImpl @Inject constructor() : MlcEngineWrapper {

    private val engine = MLCEngine()
    private var isLoaded = false
    private var currentModel = ""

    private val modelLibMap = mapOf(
        "mlc-ai_Phi-3.5-mini-instruct-q4f16_0-MLC" to "phi3_q4f16_0_5fe42298399a05eb2a1878fdc1c8c115",
        "mlc-ai_Qwen2.5-1.5B-Instruct-q4f16_1-MLC" to "qwen2_q4f16_1_2e221f430380225c03990ad24c3d030e",
        "mlc-ai_gemma-2-2b-it-q4f16_1-MLC" to "gemma2_q4f16_1_5cc7dbd3ae3d1040984d9720b2d7b7d4",
        "mlc-ai_Llama-3.2-3B-Instruct-q4f16_0-MLC" to "llama_q4f16_0_2d32572d8a4ab2af20a1f587ef6c8c63",
        "mlc-ai_Mistral-7B-Instruct-v0.3-q4f16_1-MLC" to "mistral_q4f16_1_c2cba77a6def4dd52f7e20b5d8576ab5"
    )

    override suspend fun loadModel(modelPath: String, backend: String) {
        val modelId = java.io.File(modelPath).name
        val modelLib = modelLibMap[modelId] ?: "system://$modelId"
        withContext(Dispatchers.IO) {
            engine.reload(modelPath, modelLib, backend)
        }
        isLoaded = true
        currentModel = modelPath
    }

    override suspend fun unloadModel() {
        withContext(Dispatchers.IO) {
            engine.unload()
        }
        isLoaded = false
    }

    override fun generateStream(prompt: String, temperature: Float, maxTokens: Int): Flow<String> = flow {
        if (!isLoaded) return@flow

        val messages = listOf(ChatCompletionMessage(role = ai.mlc.mlcllm.OpenAIProtocol.ChatCompletionRole.user, content = prompt))
        val responseChannel = engine.chat.completions.create(messages = messages)
        
        for (response in responseChannel) {
            val content = response.choices.firstOrNull()?.delta?.content?.asText()
            if (content != null) {
                emit(content)
            }
        }
    }

    override fun isModelLoaded(): Boolean = isLoaded

    override fun getMemoryStatus(): String {
        return "Unknown"
    }
}

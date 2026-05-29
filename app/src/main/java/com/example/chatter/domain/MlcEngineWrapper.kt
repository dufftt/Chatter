package com.example.chatter.domain

import kotlinx.coroutines.flow.Flow

interface MlcEngineWrapper {
    suspend fun loadModel(modelPath: String, backend: String = "vulkan")
    suspend fun unloadModel()
    fun generateStream(prompt: String, temperature: Float = 0.7f, maxTokens: Int = 1024): Flow<String>
    fun isModelLoaded(): Boolean
    fun getMemoryStatus(): String
}

package com.example.chatter.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class HfModelInfo(val siblings: List<HfSibling>)

@Serializable
data class HfSibling(val rfilename: String)

@Singleton
class ModelDownloader @Inject constructor(
    private val client: OkHttpClient
) {
    private val json = Json { ignoreUnknownKeys = true }

    private fun fetchModelFilesList(repoId: String): List<String> {
        val url = "https://huggingface.co/api/models/$repoId"
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Failed to fetch model info: ${response.code}")
        }
        val responseBody = response.body?.string() ?: throw Exception("Empty response body")
        val modelInfo = json.decodeFromString<HfModelInfo>(responseBody)
        return modelInfo.siblings.map { it.rfilename }.filter { 
            !it.startsWith(".") && it != "README.md" && it != "logs.txt"
        }
    }

    fun downloadHuggingFaceModel(repoId: String, destDir: File): Flow<Int> = flow {
        if (!destDir.exists()) {
            destDir.mkdirs()
        }

        val filesToDownload = fetchModelFilesList(repoId)
        if (filesToDownload.isEmpty()) {
            throw Exception("No files found to download")
        }
        
        val totalFiles = filesToDownload.size
        var completedFiles = 0

        for (filename in filesToDownload) {
            val fileUrl = "https://huggingface.co/$repoId/resolve/main/$filename"
            val destFile = File(destDir, filename)
            
            destFile.parentFile?.mkdirs()

            val request = Request.Builder().url(fileUrl).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw Exception("Failed to download $filename: ${response.code}")
            }

            val body = response.body ?: throw Exception("Empty response body for $filename")
            val contentLength = body.contentLength()
            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(destFile)
            
            var bytesCopied: Long = 0
            val buffer = ByteArray(64 * 1024)
            var bytes = inputStream.read(buffer)

            while (bytes >= 0) {
                outputStream.write(buffer, 0, bytes)
                bytesCopied += bytes
                
                val fileProgress = if (contentLength > 0) bytesCopied.toDouble() / contentLength else 0.0
                val overallProgress = (((completedFiles + fileProgress) / totalFiles) * 100).toInt()
                emit(overallProgress)
                
                bytes = inputStream.read(buffer)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
            
            completedFiles++
            emit((completedFiles * 100) / totalFiles)
        }
    }.flowOn(Dispatchers.IO)
}

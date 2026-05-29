package com.example.chatter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "models")
data class ModelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val size: Long,
    val localPath: String,
    val isDownloaded: Boolean
)

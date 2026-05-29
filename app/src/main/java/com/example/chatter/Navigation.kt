package com.example.chatter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.chatter.presentation.chat.ChatScreen
import com.example.chatter.presentation.hub.ModelHubScreen
import com.example.chatter.presentation.manager.ModelManagerScreen
import com.example.chatter.presentation.settings.SettingsScreen

enum class Screen { Chat, Hub, Manager, Settings }

@Composable
fun MainNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.Chat) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = currentScreen == Screen.Chat,
                    onClick = { currentScreen = Screen.Chat }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Download, contentDescription = "Hub") },
                    label = { Text("Hub") },
                    selected = currentScreen == Screen.Hub,
                    onClick = { currentScreen = Screen.Hub }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Memory, contentDescription = "Manager") },
                    label = { Text("Manager") },
                    selected = currentScreen == Screen.Manager,
                    onClick = { currentScreen = Screen.Manager }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = currentScreen == Screen.Settings,
                    onClick = { currentScreen = Screen.Settings }
                )
            }
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                Screen.Chat -> ChatScreen()
                Screen.Hub -> ModelHubScreen()
                Screen.Manager -> ModelManagerScreen()
                Screen.Settings -> SettingsScreen()
            }
        }
    }
}

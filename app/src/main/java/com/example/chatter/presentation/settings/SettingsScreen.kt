package com.example.chatter.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var backend by remember { mutableStateOf("Vulkan") }
    var temperature by remember { mutableFloatStateOf(0.7f) }
    var batterySaver by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Backend Selection
            Column {
                Text("Backend", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = backend == "Vulkan", onClick = { backend = "Vulkan" })
                    Text("Vulkan (GPU)")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = backend == "OpenCL", onClick = { backend = "OpenCL" })
                    Text("OpenCL (GPU)")
                }
            }

            // Temperature
            Column {
                Text("Temperature: ${String.format("%.2f", temperature)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Slider(
                    value = temperature,
                    onValueChange = { temperature = it },
                    valueRange = 0f..2f
                )
            }

            // Battery Saver
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Battery Saver (Auto-unload)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Switch(checked = batterySaver, onCheckedChange = { batterySaver = it })
            }
        }
    }
}

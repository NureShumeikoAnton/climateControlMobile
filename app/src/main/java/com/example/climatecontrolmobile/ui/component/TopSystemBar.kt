package com.example.climatecontrolmobile.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.climatecontrolmobile.data.model.System

@Composable
fun TopSystemBar(
    selectedSystem: System?,
    systems: List<System>,
    onSystemSelected: (System) -> Unit,
    onChangeProfile: () -> Unit,
    onLogout: () -> Unit = {}
) {
    var showSystemDropdown by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(
                text = "System: ",
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = { showSystemDropdown = true }) {
                Text(selectedSystem?.name ?: "Select system")
            }

            DropdownMenu(
                expanded = showSystemDropdown,
                onDismissRequest = { showSystemDropdown = false }
            ) {
                systems.forEach { system ->
                    DropdownMenuItem(
                        text = { Text(system.name) },
                        onClick = {
                            onSystemSelected(system)
                            showSystemDropdown = false
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            IconButton(onClick = { menuExpanded = true }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Change Profile") },
                    onClick = {
                        onChangeProfile()
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        onLogout()
                        menuExpanded = false
                    }
                )
            }
        }
    }
}
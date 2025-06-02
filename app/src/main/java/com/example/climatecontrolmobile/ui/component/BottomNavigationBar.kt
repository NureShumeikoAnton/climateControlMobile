package com.example.climatecontrolmobile.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.climatecontrolmobile.ui.viewmodel.MainViewModel

@Composable
fun BottomNavigationBar(
    currentTab: MainViewModel.Tab,
    onTabSelected: (MainViewModel.Tab) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Sensors") },
            label = { Text("Sensors") },
            selected = currentTab == MainViewModel.Tab.SENSORS,
            onClick = { onTabSelected(MainViewModel.Tab.SENSORS) }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Devices") },
            label = { Text("Devices") },
            selected = currentTab == MainViewModel.Tab.DEVICES,
            onClick = { onTabSelected(MainViewModel.Tab.DEVICES) }
        )
    }
}
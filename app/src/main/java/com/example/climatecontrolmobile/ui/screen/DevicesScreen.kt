package com.example.climatecontrolmobile.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.climatecontrolmobile.data.model.Command
import com.example.climatecontrolmobile.data.model.Device
import com.example.climatecontrolmobile.ui.component.DeviceItem
import com.example.climatecontrolmobile.ui.component.SensorItem
import com.example.climatecontrolmobile.ui.viewmodel.MainViewModel

@Composable
fun DevicesScreen(
    devicesWithCommands: List<Pair<Device, Command?>>,
    onDeviceClick: (Int) -> Unit = {}
) {
    if (devicesWithCommands.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No devices found")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(devicesWithCommands) { (device, command) ->
                DeviceItem(
                    device = device,
                    lastCommand = command,
                    onClick = { onDeviceClick(device.deviceId) }
                )
            }
        }
    }
}

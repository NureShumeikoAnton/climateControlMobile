package com.example.climatecontrolmobile.data.repository

import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.Command
import com.example.climatecontrolmobile.data.model.Device
import com.example.climatecontrolmobile.data.model.Measurement
import com.example.climatecontrolmobile.data.model.Sensor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class DeviceRepository {
    private val apiService = NetworkClient.apiService

    suspend fun getDevices(): List<Device> = withContext(Dispatchers.IO) {
        apiService.getDevices()
    }

    suspend fun getDevicesBySystemId(systemId: Int): List<Device> = withContext(Dispatchers.IO) {
        apiService.getDevicesBySystemId(systemId)
    }

    suspend fun getDevicesWithLastCommandBySystemId(systemId: Int): List<Pair<Device, Command?>> = withContext(Dispatchers.IO) {
        val devices = getDevicesBySystemId(systemId)
        devices.map { device ->
            val commands = apiService.getCommands(device.deviceId)
            val lastCommand = commands.maxByOrNull { it.createdAt }
            Pair(device, lastCommand)
        }
    }
}
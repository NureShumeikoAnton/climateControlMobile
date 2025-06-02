package com.example.climatecontrolmobile.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.Command
import com.example.climatecontrolmobile.data.model.Device
import com.example.climatecontrolmobile.data.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceDetailViewModel(context: Context, private val deviceId: Int) : ViewModel() {
    private val deviceRepository = DeviceRepository()
    
    init {
        NetworkClient.initialize(context)
    }

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> = _device.asStateFlow()

    private val _commands = MutableStateFlow<List<Command>>(emptyList())
    val commands: StateFlow<List<Command>> = _commands.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadDeviceDetails()
    }

    private fun loadDeviceDetails() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val commands = NetworkClient.apiService.getCommands(deviceId)

                val sortedCommands = commands
                    .sortedByDescending { it.createdAt }
                    .take(10)
                
                _commands.value = sortedCommands

                val devices = deviceRepository.getDevices()
                val deviceDetail = devices.find { it.deviceId == deviceId }
                _device.value = deviceDetail
                
            } catch (e: Exception) {
                _error.value = "Failed to load device details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadDeviceDetails()
    }
}

package com.example.climatecontrolmobile.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.Command
import com.example.climatecontrolmobile.data.model.Device
import com.example.climatecontrolmobile.data.model.Measurement
import com.example.climatecontrolmobile.data.model.Profile
import com.example.climatecontrolmobile.data.model.Sensor
import com.example.climatecontrolmobile.data.model.System
import com.example.climatecontrolmobile.data.repository.DeviceRepository
import com.example.climatecontrolmobile.data.repository.ProfileRepository
import com.example.climatecontrolmobile.data.repository.SensorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {
    private val sensorRepository = SensorRepository()
    private val deviceRepository = DeviceRepository()
    private val profileRepository = ProfileRepository()
    
    init {
        NetworkClient.initialize(context)
    }

    private val _sensorsWithMeasurements = MutableStateFlow<List<Pair<Sensor, Measurement?>>>(emptyList())
    val sensorsWithMeasurements: StateFlow<List<Pair<Sensor, Measurement?>>> = _sensorsWithMeasurements.asStateFlow()

    private val _devicesWithCommands = MutableStateFlow<List<Pair<Device, Command?>>>(emptyList())
    val devicesWithCommands: StateFlow<List<Pair<Device, Command?>>> = _devicesWithCommands.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedSystem = MutableStateFlow<System?>(null)
    val selectedSystem: StateFlow<System?> = _selectedSystem.asStateFlow()

    private val _systems = MutableStateFlow<List<System>>(emptyList())
    val systems: StateFlow<List<System>> = _systems.asStateFlow()

    private val _currentTab = MutableStateFlow(Tab.SENSORS)
    val currentTab: StateFlow<Tab> = _currentTab.asStateFlow()

    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles.asStateFlow()

    private val _showProfileModal = MutableStateFlow(false)
    val showProfileModal: StateFlow<Boolean> = _showProfileModal.asStateFlow()

    fun loadSystems() {
        viewModelScope.launch {
            try {
                val systemsList = sensorRepository.getUserSystems()
                _systems.value = systemsList
                if (systemsList.isNotEmpty() && _selectedSystem.value == null) {
                    _selectedSystem.value = systemsList.first()
                }
                loadSensors()
            } catch (e: Exception) {
                _error.value = "Failed to load systems: ${e.message}"
            }
        }
    }

    fun loadSensors() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val sensorsWithLastMeasurement = sensorRepository.getSensorsWithLastMeasurementBySystemId(_selectedSystem.value?.systemId ?: 0)
                _sensorsWithMeasurements.value = sensorsWithLastMeasurement
            } catch (e: Exception) {
                _error.value = "Failed to load sensors: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadDevices() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val devicesWithLastCommand = deviceRepository.getDevicesWithLastCommandBySystemId(_selectedSystem.value?.systemId ?: 0)
                _devicesWithCommands.value = devicesWithLastCommand
            } catch (e: Exception) {
                _error.value = "Failed to load devices: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectSystem(system: System) {
        _selectedSystem.value = system
        loadSensors()
        loadDevices()
    }

    fun setCurrentTab(tab: Tab) {
        _currentTab.value = tab
    }

    fun showProfileModal() {
        _showProfileModal.value = true
        loadProfiles()
    }

    fun hideProfileModal() {
        _showProfileModal.value = false
    }

    fun loadProfiles() {
        viewModelScope.launch {
            try {
                _selectedSystem.value?.let { system ->
                    val profilesList = profileRepository.getProfilesBySystemId(system.systemId)
                    _profiles.value = profilesList
                }
            } catch (e: Exception) {
                _error.value = "Failed to load profiles: ${e.message}"
            }
        }
    }

    fun changeProfile(profileId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _selectedSystem.value?.let { system ->
                    val updatedSystem = profileRepository.updateSystemProfile(system.systemId, profileId)
                    updatedSystem?.let {
                        _selectedSystem.value = it
                        // Update the system in the systems list
                        _systems.value = _systems.value.map { s ->
                            if (s.systemId == it.systemId) it else s
                        }
                        _showProfileModal.value = false
                        // Reload data after profile change
                        loadSensors()
                        loadDevices()
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to change profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    enum class Tab {
        SENSORS, DEVICES
    }
}
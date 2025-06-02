package com.example.climatecontrolmobile.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.Measurement
import com.example.climatecontrolmobile.data.model.Sensor
import com.example.climatecontrolmobile.data.repository.SensorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SensorDetailViewModel(context: Context, private val sensorId: Int) : ViewModel() {
    private val sensorRepository = SensorRepository()
    
    init {
        NetworkClient.initialize(context)
    }

    private val _sensor = MutableStateFlow<Sensor?>(null)
    val sensor: StateFlow<Sensor?> = _sensor.asStateFlow()

    private val _measurements = MutableStateFlow<List<Measurement>>(emptyList())
    val measurements: StateFlow<List<Measurement>> = _measurements.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadSensorDetails()
    }

    private fun loadSensorDetails() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val measurements = sensorRepository.getMeasurements(sensorId)

                val sortedMeasurements = measurements
                    .sortedByDescending { it.createdAt }
                    .take(10)
                
                _measurements.value = sortedMeasurements

                val sensors = sensorRepository.getSensors()
                val sensorDetail = sensors.find { it.sensorId == sensorId }
                _sensor.value = sensorDetail
                
            } catch (e: Exception) {
                _error.value = "Failed to load sensor details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadSensorDetails()
    }
}

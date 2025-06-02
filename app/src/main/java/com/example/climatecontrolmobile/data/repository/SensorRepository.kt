package com.example.climatecontrolmobile.data.repository

import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.Measurement
import com.example.climatecontrolmobile.data.model.Sensor
import com.example.climatecontrolmobile.data.model.System
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class SensorRepository {
    private val apiService = NetworkClient.apiService

    suspend fun getSensors(): List<Sensor> = withContext(Dispatchers.IO) {
        apiService.getSensors()
    }

    suspend fun getSensorsBySystemId(systemId: Int): List<Sensor> = withContext(Dispatchers.IO) {
        apiService.getSensorsBySystemId(systemId)
    }

    suspend fun getMeasurements(sensorId: Int): List<Measurement> = withContext(Dispatchers.IO) {
        apiService.getMeasurements(sensorId)
    }

    suspend fun getSensorsWithLastMeasurementBySystemId(systemId: Int): List<Pair<Sensor, Measurement?>> = withContext(Dispatchers.IO) {
        val sensors = getSensorsBySystemId(systemId)
        sensors.map { sensor ->
            val measurements = apiService.getMeasurements(sensor.sensorId)
            val lastMeasurement = measurements.maxByOrNull { it.createdAt }
            Pair(sensor, lastMeasurement)
        }
    }

    suspend fun getSystems(): List<System> = withContext(Dispatchers.IO) {
        apiService.getSystems()
    }

    suspend fun getUserSystems(): List<System> = withContext(Dispatchers.IO) {
        apiService.getUserSystems()
    }
}

package com.example.climatecontrolmobile.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.climatecontrolmobile.data.model.Measurement
import com.example.climatecontrolmobile.data.model.Sensor
import com.example.climatecontrolmobile.ui.component.SensorItem

@Composable
fun SensorsScreen(
    sensorsWithMeasurements: List<Pair<Sensor, Measurement?>>,
    onSensorClick: (Int) -> Unit = {}
) {
    if (sensorsWithMeasurements.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No sensors found")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(sensorsWithMeasurements) { (sensor, measurement) ->
                SensorItem(
                    sensor = sensor,
                    lastMeasurement = measurement,
                    onClick = { onSensorClick(sensor.sensorId) }
                )
            }
        }
    }
}

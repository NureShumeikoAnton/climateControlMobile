package com.example.climatecontrolmobile.data.model

import java.util.Date

data class Measurement(
    val measurementId: Int,
    val value: Double,
    val sensorId: Int,
    val createdAt: Date
)
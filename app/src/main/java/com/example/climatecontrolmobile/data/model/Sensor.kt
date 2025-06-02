package com.example.climatecontrolmobile.data.model

import java.util.Date

data class Sensor(
    val sensorId: Int,
    val serial: String,
    val name: String,
    val type: String,
    val systemId: Int,
    val createdAt: Date,
    val lastSync: Date
)
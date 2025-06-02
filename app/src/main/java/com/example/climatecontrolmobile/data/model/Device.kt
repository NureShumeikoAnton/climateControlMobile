package com.example.climatecontrolmobile.data.model

import java.util.Date

data class Device(
    val deviceId: Int,
    val serial: String,
    val name: String,
    val power: Double,
    val mode: String,
    val type: String,
    val systemId: Int,
    val createdAt: Date
)

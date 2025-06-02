package com.example.climatecontrolmobile.data.model

import java.util.Date

data class Command(
    val commandId: Int,
    val value: Double,
    val deviceId: Int,
    val createdAt: Date,
)

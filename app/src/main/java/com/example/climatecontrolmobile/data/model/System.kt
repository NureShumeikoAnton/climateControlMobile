package com.example.climatecontrolmobile.data.model

import java.util.Date

data class System(
    val systemId: Int,
    val name: String,
    val profileId: Int,
    val createdAt: Date,
)
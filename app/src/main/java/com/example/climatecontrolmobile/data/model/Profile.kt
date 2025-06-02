package com.example.climatecontrolmobile.data.model

import java.util.Date

data class Profile(
    val profileId: Int,
    val name: String,
    val createdAt: Date,
    val userId: Int,
    val systemId: Int
)

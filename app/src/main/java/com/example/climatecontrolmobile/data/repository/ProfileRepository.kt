package com.example.climatecontrolmobile.data.repository

import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.Profile
import com.example.climatecontrolmobile.data.model.System
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository {
    private val apiService = NetworkClient.apiService

    suspend fun getProfilesBySystemId(systemId: Int): List<Profile> = withContext(Dispatchers.IO) {
        apiService.getProfilesBySystemId(systemId)
    }

    suspend fun updateSystemProfile(systemId: Int, profileId: Int): System? = withContext(Dispatchers.IO) {
        val response = apiService.updateSystemProfile(systemId, profileId)
        if (response.isSuccessful) {
            response.body()
        } else {
            throw Exception("Failed to update profile: ${response.message()}")
        }
    }
}

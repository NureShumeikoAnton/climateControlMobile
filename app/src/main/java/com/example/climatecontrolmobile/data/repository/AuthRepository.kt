package com.example.climatecontrolmobile.data.repository

import android.content.Context
import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.LoginRequest
import com.example.climatecontrolmobile.data.storage.TokenManager
import com.example.climatecontrolmobile.utils.FCMHelper

class AuthRepository(private val context: Context) {
    private val apiService = NetworkClient.apiService
    private val tokenManager = TokenManager(context)

    suspend fun login(email: String, password: String): String {
        val loginRequest = LoginRequest(email, password)
        val response = apiService.login(loginRequest)
        if (response.isSuccessful) {
            return response.body()?.token ?: throw Exception("Invalid response from server")
        } else {
            throw Exception("Authentication failed: ${response.message()}")
        }
    }

    fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }

    fun logout() {
        tokenManager.clearToken()
        tokenManager.clearFcmToken()
    }

    suspend fun initializeFcmAfterLogin() {
        try {
            FCMHelper.initializeFCM(context)
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Failed to initialize FCM after login", e)
        }
    }

    fun isAuthenticated(): Boolean {
        return tokenManager.getToken() != null
    }
}
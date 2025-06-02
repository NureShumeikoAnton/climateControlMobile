package com.example.climatecontrolmobile.data.repository

import android.content.Context
import android.util.Log
import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.model.FcmTokenRequest
import com.example.climatecontrolmobile.data.storage.TokenManager
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FCMRepository(context: Context) {
    private val apiService = NetworkClient.apiService
    private val tokenManager = TokenManager(context)

    suspend fun getFcmToken(): String = withContext(Dispatchers.IO) {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d(TAG, "FCM Token retrieved: $token")
            saveFcmTokenLocally(token)
            return@withContext token
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get FCM token", e)
            throw e
        }
    }

    fun saveFcmTokenLocally(token: String) {
        tokenManager.saveFcmToken(token)
        Log.d(TAG, "FCM token saved locally")
    }

    fun getFcmTokenLocally(): String? {
        return tokenManager.getFcmToken()
    }    suspend fun sendFcmTokenToServer(fcmToken: String) = withContext(Dispatchers.IO) {
        try {
            val request = FcmTokenRequest(fcmToken)
            val response = apiService.updateFcmToken(request)
            if (response.isSuccessful) {
                Log.d(TAG, "FCM token sent to server successfully")
            } else {
                throw Exception("Failed to send FCM token: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending FCM token to server", e)
            throw e
        }
    }

    suspend fun initializeFcm() {
        try {
            val token = getFcmToken()
            sendFcmTokenToServer(token)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize FCM", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "FCMRepository"
    }
}

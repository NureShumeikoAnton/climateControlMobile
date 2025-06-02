package com.example.climatecontrolmobile.utils

import android.content.Context
import android.util.Log
import com.example.climatecontrolmobile.data.repository.FCMRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FCMHelper {
    private const val TAG = "FCMHelper"

    fun initializeFCM(context: Context) {
        val fcmRepository = FCMRepository(context)
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existingToken = fcmRepository.getFcmTokenLocally()
                if (existingToken != null) {
                    Log.d(TAG, "Found existing FCM token, sending to server")
                    fcmRepository.sendFcmTokenToServer(existingToken)
                } else {
                    Log.d(TAG, "No existing FCM token, getting new one")
                    fcmRepository.initializeFcm()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize FCM", e)
            }
        }
    }

    fun refreshFCMToken(context: Context) {
        val fcmRepository = FCMRepository(context)
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Refreshing FCM token")
                fcmRepository.initializeFcm()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to refresh FCM token", e)
            }
        }
    }
}

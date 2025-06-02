package com.example.climatecontrolmobile

import android.app.Application
import android.util.Log
import com.example.climatecontrolmobile.data.api.NetworkClient
import com.example.climatecontrolmobile.data.storage.TokenManager

class ClimateControlApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkClient.initialize(this)

        val tokenManager = TokenManager(this)
        val jwtToken = tokenManager.getToken()
        
        if (jwtToken != null) {
            Log.d("Application", "User is logged in, FCM will be initialized after authentication")
        } else {
            Log.d("Application", "User not logged in, FCM will be initialized after login")
        }
    }
}

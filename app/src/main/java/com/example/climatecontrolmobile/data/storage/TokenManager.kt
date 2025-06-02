package com.example.climatecontrolmobile.data.storage

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("jwt_token").apply()
    }

    fun saveFcmToken(fcmToken: String) {
        sharedPreferences.edit().putString("fcm_token", fcmToken).apply()
    }

    fun getFcmToken(): String? {
        return sharedPreferences.getString("fcm_token", null)
    }

    fun clearFcmToken() {
        sharedPreferences.edit().remove("fcm_token").apply()
    }
}
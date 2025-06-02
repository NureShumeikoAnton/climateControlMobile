package com.example.climatecontrolmobile.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.climatecontrolmobile.MainActivity
import com.example.climatecontrolmobile.R
import com.example.climatecontrolmobile.data.repository.FCMRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FCMService : FirebaseMessagingService() {

    private val fcmRepository by lazy { FCMRepository(applicationContext) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.title ?: "Climate Control", it.body ?: "")
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        
        // Send token to server
        CoroutineScope(Dispatchers.IO).launch {
            try {
                fcmRepository.saveFcmTokenLocally(token)
                fcmRepository.sendFcmTokenToServer(token)
                Log.d(TAG, "FCM token sent to server successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send FCM token to server", e)
            }
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        // Handle custom data payload here
        // For example: system alerts, temperature warnings, etc.
        val alertType = data["alert_type"]
        val systemId = data["system_id"]
        val message = data["message"]
        
        when (alertType) {
            "temperature_warning" -> {
                sendNotification("Temperature Alert", message ?: "Temperature threshold exceeded")
            }
            "system_offline" -> {
                sendNotification("System Offline", message ?: "Climate system is offline")
            }
            "profile_changed" -> {
                sendNotification("Profile Updated", message ?: "System profile has been changed")
            }
            else -> {
                sendNotification("Climate Control", message ?: "New notification")
            }
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = "climate_control_notifications"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Climate Control Notifications",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FCMService"
    }
}

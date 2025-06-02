# Firebase Cloud Messaging (FCM) Setup Instructions

## Android App Setup Complete ✅

The Android FCM integration is now complete with the following components:

### Implemented Features:
1. **FCM Service** - Handles incoming push notifications
2. **FCM Repository** - Manages token generation and server communication
3. **Token Management** - Local storage and server synchronization
4. **Notification Permissions** - Android 13+ permission handling
5. **Test Interface** - Manual FCM token refresh via UI

## Next Steps: Firebase Console Setup

### 1. Firebase Project Setup
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing project
3. Enable Cloud Messaging in the project

### 2. Add Android App
1. Click "Add app" → Android
2. Package name: `com.example.climatecontrolmobile`
3. App nickname: `Climate Control Mobile`
4. Download `google-services.json`
5. Replace `app/google-services.json.template` with the real file

### 3. Server-side Implementation Required

Your server needs to implement this endpoint:

```http
PUT /users/fcm-token
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "fcmToken": "firebase_token_here"
}
```

### 4. Database Schema Update

Add fcm_token column to your users table:
```sql
ALTER TABLE users ADD COLUMN fcm_token VARCHAR(255);
```

### 5. Server-side Notification Sending

Install Firebase Admin SDK on your server and implement:

```java
// Example Java Spring Boot implementation
@Service
public class PushNotificationService {
    
    public void sendNotification(String fcmToken, String title, String body, Map<String, String> data) {
        Message message = Message.builder()
            .setToken(fcmToken)
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .putAllData(data)
            .build();
            
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}
```

## Testing the Implementation

### 1. Build and Run the App
```bash
./gradlew assembleDebug
```

### 2. Test FCM Token Generation
1. Log into the app
2. Go to main screen
3. Click menu (⋮) → "Test FCM"
4. Check logs for FCM token generation
5. Verify token is sent to your server

### 3. Check Logs
Look for these log messages:
- `FCMService: Refreshed token: [token]`
- `FCMRepository: FCM token sent to server successfully`

### 4. Test Push Notifications
Use Firebase Console "Cloud Messaging" section to send test notifications to your app.

## Notification Types Supported

The app handles these notification types:

1. **Simple Notifications** - Title + body
2. **Data Messages** - Custom payload handling:
   - `alert_type: "temperature_warning"`
   - `alert_type: "system_offline"`
   - `alert_type: "profile_changed"`
   - Custom `system_id` and `message` fields

## Troubleshooting

### Common Issues:
1. **Missing google-services.json** - Download from Firebase Console
2. **Token not generated** - Check notification permissions
3. **Server endpoint 404** - Implement the PUT /users/fcm-token endpoint
4. **Notifications not received** - Check FCM token validity

### Debug Commands:
```bash
# Check if app can receive notifications
adb shell dumpsys notification

# View app logs
adb logcat | grep -E "(FCM|Firebase|ClimateControl)"
```

## Production Considerations

1. **Token Refresh** - Implement periodic token refresh
2. **Error Handling** - Handle network failures gracefully
3. **User Preferences** - Allow users to disable notifications
4. **Analytics** - Track notification delivery and engagement

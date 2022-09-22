package com.gaushala.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    val TAG = "FCM_FIREBASE_MESSAGING"
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "onNewToken: $p0" )
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d(TAG, "onMessageReceived: ${p0.notification?.body}" )
    }
}
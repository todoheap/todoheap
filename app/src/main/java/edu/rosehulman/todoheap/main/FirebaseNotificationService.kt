package edu.rosehulman.todoheap.main

import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseNotificationService:FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}
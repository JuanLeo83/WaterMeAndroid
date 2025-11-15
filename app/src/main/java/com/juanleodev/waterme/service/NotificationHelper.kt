package com.juanleodev.waterme.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.juanleodev.waterme.R

/**
 * Helper class for managing notifications.
 * Handles notification channel creation and sending notifications.
 */
class NotificationHelper(private val context: Context) {
    
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    init {
        createNotificationChannel()
    }
    
    /**
     * Creates the notification channel for watering reminders.
     * Required for Android O and above (minSdk is 29).
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Watering Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications to remind you to water your plants"
        }
        notificationManager.createNotificationChannel(channel)
    }
    
    /**
     * Sends a notification to water a specific plant.
     * TODO: Implement full logic in Historia 3
     */
    fun sendWateringReminder(plantId: Long, plantName: String) {
        // TODO: Implement notification building and sending
        // Will include:
        // - Plant name in the title
        // - Tap to open plant details
        // - "Mark as watered" action button
    }
    
    companion object {
        private const val CHANNEL_ID = "watering_reminders"
        private const val NOTIFICATION_ID_BASE = 1000
    }
}

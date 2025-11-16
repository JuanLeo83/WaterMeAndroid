package com.juanleodev.waterme.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.juanleodev.waterme.MainActivity
import com.juanleodev.waterme.R
import java.time.LocalDate
import java.time.temporal.ChronoUnit

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
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        notificationManager.createNotificationChannel(channel)
    }
    
    /**
     * Sends a notification to water a specific plant.
     * 
     * @param plantId The ID of the plant that needs watering
     * @param plantName The name of the plant to display in the notification
     * @param lastWateringDate The date when the plant was last watered
     * @param nextWateringDate The date when the plant should be watered
     */
    fun sendWateringReminder(
        plantId: Long, 
        plantName: String, 
        lastWateringDate: LocalDate,
        nextWateringDate: LocalDate
    ) {
        val today = LocalDate.now()
        val daysOverdue = ChronoUnit.DAYS.between(nextWateringDate, today).toInt()
        
        // Create intent to open the app when notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_PLANT_ID, plantId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            plantId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Create intent for "Mark as Watered" action
        val waterActionIntent = Intent(context, WaterPlantReceiver::class.java).apply {
            putExtra(EXTRA_PLANT_ID, plantId)
        }
        
        val waterActionPendingIntent = PendingIntent.getBroadcast(
            context,
            plantId.toInt() + ACTION_OFFSET,
            waterActionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Determine the notification message based on whether the plant is overdue
        val message = if (daysOverdue > 0) {
            context.getString(R.string.notification_message_overdue, daysOverdue)
        } else {
            context.getString(R.string.notification_message)
        }
        
        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // You may want to create a custom icon
            .setContentTitle(context.getString(R.string.notification_title, plantName))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_launcher_foreground, // You may want to create a custom icon
                context.getString(R.string.notification_action_water),
                waterActionPendingIntent
            )
            .build()
        
        // Use plant ID as the notification ID to ensure one notification per plant
        notificationManager.notify(NOTIFICATION_ID_BASE + plantId.toInt(), notification)
    }
    
    /**
     * Cancels a notification for a specific plant.
     * This is useful when a plant is watered or deleted.
     */
    fun cancelNotification(plantId: Long) {
        notificationManager.cancel(NOTIFICATION_ID_BASE + plantId.toInt())
    }
    
    companion object {
        private const val CHANNEL_ID = "watering_reminders"
        private const val NOTIFICATION_ID_BASE = 1000
        private const val ACTION_OFFSET = 10000
        const val EXTRA_PLANT_ID = "plant_id"
    }
}

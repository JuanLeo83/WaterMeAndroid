package com.juanleodev.waterme.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.juanleodev.waterme.data.PlantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * BroadcastReceiver to handle the "Mark as Watered" action from notifications.
 * When user taps the action button in the notification, this receiver will:
 * 1. Update the plant's lastWateringDate to today
 * 2. Cancel the notification
 */
class WaterPlantReceiver : BroadcastReceiver(), KoinComponent {
    
    private val plantRepository: PlantRepository by inject()
    private val notificationHelper: NotificationHelper by inject()
    
    override fun onReceive(context: Context, intent: Intent) {
        val plantId = intent.getLongExtra(NotificationHelper.EXTRA_PLANT_ID, -1)
        
        if (plantId != -1L) {
            // Use a coroutine to perform the async operation
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Mark the plant as watered
                    plantRepository.markPlantAsWatered(plantId)
                    
                    // Cancel the notification
                    notificationHelper.cancelNotification(plantId)
                } catch (e: Exception) {
                    // Log error but don't crash the app
                    e.printStackTrace()
                }
            }
        }
    }
}

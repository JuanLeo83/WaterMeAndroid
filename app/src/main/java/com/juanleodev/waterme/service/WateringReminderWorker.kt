package com.juanleodev.waterme.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juanleodev.waterme.data.PlantRepository
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

/**
 * Worker for checking plants that need watering and sending notifications.
 * This worker runs daily to check if any plants need to be watered.
 * 
 * Logic:
 * - Runs once per day (configured in WaterMeApplication)
 * - Gets all plants from the repository
 * - For each plant, checks if today >= nextWateringDate
 * - If yes, sends a notification
 * - Notifications will repeat daily until the plant is marked as watered
 */
class WateringReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    
    private val plantRepository: PlantRepository by inject()
    private val notificationHelper: NotificationHelper by inject()
    
    override suspend fun doWork(): Result {
        return try {
            // Get all plants
            val plants = plantRepository.allPlants.first()
            
            val today = LocalDate.now()
            
            // Check each plant to see if it needs watering
            plants.forEach { plant ->
                val nextWateringDate = plant.getNextWateringDate()
                
                // If today is on or after the next watering date, send notification
                if (!today.isBefore(nextWateringDate)) {
                    notificationHelper.sendWateringReminder(
                        plantId = plant.id,
                        plantName = plant.name,
                        lastWateringDate = plant.lastWateringDate,
                        nextWateringDate = nextWateringDate
                    )
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            // Log the error and retry
            e.printStackTrace()
            Result.retry()
        }
    }
    
    companion object {
        const val WORK_NAME = "WateringReminderWork"
        const val TAG = "WateringReminder"
    }
}

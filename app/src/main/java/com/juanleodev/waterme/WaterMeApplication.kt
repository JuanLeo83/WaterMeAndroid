package com.juanleodev.waterme

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.juanleodev.waterme.di.appModule
import com.juanleodev.waterme.service.WateringReminderWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.concurrent.TimeUnit

class WaterMeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@WaterMeApplication)
            modules(appModule)
        }
        
        // Schedule the daily watering reminder worker
        scheduleWateringReminders()
    }
    
    /**
     * Schedules a periodic worker to check for plants that need watering.
     * The worker runs once per day.
     */
    private fun scheduleWateringReminders() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false) // Run even if battery is low
            .build()
        
        // Create a periodic work request that runs once per day
        // Using the minimum interval of 15 minutes for testing purposes
        // In production, this should be 1 day (24 hours)
        val workRequest = PeriodicWorkRequestBuilder<WateringReminderWorker>(
            15, TimeUnit.MINUTES // For testing: check every 15 minutes
            // 1, TimeUnit.DAYS // For production: check once per day
        )
            .setConstraints(constraints)
            .addTag(WateringReminderWorker.TAG)
            .build()
        
        // Enqueue the work, replacing any existing work with the same name
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WateringReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if it exists
            workRequest
        )
    }
}

package com.juanleodev.waterme.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.juanleodev.waterme.R
import com.juanleodev.waterme.data.db.Plant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Represents the watering status of a plant based on its watering schedule.
 */
enum class WateringStatus(
    @param:StringRes val displayNameRes: Int,
    val color: Color,
    @param:StringRes val descriptionRes: Int
) {
    /**
     * Plant was watered recently and doesn't need watering yet.
     */
    HEALTHY(
        displayNameRes = R.string.watering_status_healthy,
        color = Color(0xFF4CAF50), // Green
        descriptionRes = R.string.watering_status_healthy_desc
    ),
    
    /**
     * Plant needs watering today or soon.
     */
    NEEDS_WATER(
        displayNameRes = R.string.watering_status_needs_water,
        color = Color(0xFFFF9800), // Orange
        descriptionRes = R.string.watering_status_needs_water_desc
    ),
    
    /**
     * Plant is overdue for watering.
     */
    OVERDUE(
        displayNameRes = R.string.watering_status_overdue,
        color = Color(0xFFF44336), // Red
        descriptionRes = R.string.watering_status_overdue_desc
    );
    
    companion object {
        /**
         * Calculate the watering status for a plant based on current date.
         * 
         * - HEALTHY (Green): Plant doesn't need watering yet (within schedule)
         * - NEEDS_WATER (Orange): Today or 1-2 days overdue
         * - OVERDUE (Red): 3 or more days overdue
         */
        fun fromPlant(plant: Plant): WateringStatus {
            val today = LocalDate.now()
            val nextWateringDate = plant.getNextWateringDate()
            val daysDifference = ChronoUnit.DAYS.between(today, nextWateringDate)
            
            return when {
                daysDifference > 0 -> HEALTHY
                daysDifference >= -2 -> NEEDS_WATER  // Today (0) or 1-2 days late (-1, -2)
                else -> OVERDUE  // 3+ days late (-3 or less)
            }
        }
    }
}
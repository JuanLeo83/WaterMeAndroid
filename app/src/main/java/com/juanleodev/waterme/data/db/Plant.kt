package com.juanleodev.waterme.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

/**
 * Plant entity representing a plant in the database.
 * 
 * @property id Unique identifier for the plant
 * @property name Name of the plant (required)
 * @property photoUri Optional URI to the plant's photo
 * @property wateringFrequencyDays How often the plant needs watering (1-90 days)
 * @property reminderTime Time of day to send watering reminders
 * @property lastWateringDate Date when the plant was last watered
 */
@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    
    val photoUri: String? = null,
    
    val wateringFrequencyDays: Int, // 1-90 days
    
    val reminderTime: LocalTime,
    
    val lastWateringDate: LocalDate
) {
    /**
     * Calculates the next watering date based on last watering and frequency.
     */
    fun getNextWateringDate(): LocalDate {
        return lastWateringDate.plusDays(wateringFrequencyDays.toLong())
    }
    
    /**
     * Returns the number of days since the last watering.
     */
    fun getDaysSinceLastWatering(): Int {
        return java.time.temporal.ChronoUnit.DAYS.between(lastWateringDate, LocalDate.now()).toInt()
    }
}

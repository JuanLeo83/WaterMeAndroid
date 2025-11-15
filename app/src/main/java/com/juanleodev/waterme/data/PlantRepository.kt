package com.juanleodev.waterme.data

import com.juanleodev.waterme.data.db.Plant
import com.juanleodev.waterme.data.db.PlantDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository for managing plant data.
 * Provides a clean API for the rest of the app to access plant data.
 */
class PlantRepository(private val plantDao: PlantDao) {
    
    /**
     * Get all plants as a Flow for reactive updates.
     */
    val allPlants: Flow<List<Plant>> = plantDao.getAllPlants()
    
    /**
     * Get a specific plant by ID.
     */
    suspend fun getPlantById(plantId: Long): Plant? {
        return plantDao.getPlantById(plantId)
    }
    
    /**
     * Insert a new plant.
     * @return The ID of the inserted plant
     */
    suspend fun insertPlant(plant: Plant): Long {
        return plantDao.insertPlant(plant)
    }
    
    /**
     * Update an existing plant.
     */
    suspend fun updatePlant(plant: Plant) {
        plantDao.updatePlant(plant)
    }
    
    /**
     * Mark a plant as watered (updates lastWateringDate to today).
     */
    suspend fun markPlantAsWatered(plantId: Long) {
        val plant = plantDao.getPlantById(plantId)
        plant?.let {
            val updatedPlant = it.copy(lastWateringDate = LocalDate.now())
            plantDao.updatePlant(updatedPlant)
        }
    }
    
    /**
     * Delete a plant.
     */
    suspend fun deletePlant(plant: Plant) {
        plantDao.deletePlant(plant)
    }
    
    /**
     * Delete all plants (useful for testing).
     */
    suspend fun deleteAllPlants() {
        plantDao.deleteAllPlants()
    }
}

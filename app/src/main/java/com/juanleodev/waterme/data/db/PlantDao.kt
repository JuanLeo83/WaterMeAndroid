package com.juanleodev.waterme.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Plant entity.
 * Provides methods to interact with the plants table.
 */
@Dao
interface PlantDao {
    
    /**
     * Get all plants as a Flow for reactive updates.
     */
    @Query("SELECT * FROM plants ORDER BY name ASC")
    fun getAllPlants(): Flow<List<Plant>>
    
    /**
     * Get a specific plant by ID.
     */
    @Query("SELECT * FROM plants WHERE id = :plantId")
    suspend fun getPlantById(plantId: Long): Plant?
    
    /**
     * Insert a new plant.
     * @return The ID of the inserted plant
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant): Long
    
    /**
     * Update an existing plant.
     */
    @Update
    suspend fun updatePlant(plant: Plant)
    
    /**
     * Delete a plant.
     */
    @Delete
    suspend fun deletePlant(plant: Plant)
    
    /**
     * Delete all plants (useful for testing).
     */
    @Query("DELETE FROM plants")
    suspend fun deleteAllPlants()
}

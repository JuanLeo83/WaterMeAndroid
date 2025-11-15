package com.juanleodev.waterme.ui.screens.list

import com.juanleodev.waterme.data.db.Plant
import com.juanleodev.waterme.data.model.WateringStatus

/**
 * Plant with its computed watering status.
 */
data class PlantWithStatus(
    val plant: Plant,
    val status: WateringStatus
)
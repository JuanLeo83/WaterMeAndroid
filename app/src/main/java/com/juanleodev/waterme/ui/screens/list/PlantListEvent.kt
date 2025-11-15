package com.juanleodev.waterme.ui.screens.list

/**
 * Events that can be triggered from the UI.
 */
sealed interface PlantListEvent {
    data class WaterPlant(val plantId: Long) : PlantListEvent
    data object RefreshPlants : PlantListEvent
}
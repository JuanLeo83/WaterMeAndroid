package com.juanleodev.waterme.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanleodev.waterme.R
import com.juanleodev.waterme.data.PlantRepository
import com.juanleodev.waterme.data.model.WateringStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for PlantListScreen following MVI pattern.
 * 
 * Manages the state of the plant list, handles watering events,
 * and provides plant data with computed watering status.
 */
class PlantListViewModel(
    private val plantRepository: PlantRepository,
) : ViewModel() {
    
    // MVI State
    private val _uiState = MutableStateFlow(PlantListUiState())
    val uiState: StateFlow<PlantListUiState> = _uiState.asStateFlow()
    
    init {
        loadPlants()
    }
    
    // MVI Events
    fun handleEvent(event: PlantListEvent) {
        when (event) {
            is PlantListEvent.WaterPlant -> waterPlant(event.plantId)
            is PlantListEvent.RefreshPlants -> loadPlants()
        }
    }
    
    /**
     * Load all plants from repository and update UI state.
     */
    private fun loadPlants() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            plantRepository.allPlants
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = R.string.error_loading_plants,
                        plants = emptyList()
                    )
                }
                .collect { plants ->
                    val plantsWithStatus = plants.map { plant ->
                        PlantWithStatus(
                            plant = plant,
                            status = WateringStatus.fromPlant(plant)
                        )
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        plants = plantsWithStatus
                    )
                }
        }
    }
    
    /**
     * Mark a plant as watered and update the state.
     */
    private fun waterPlant(plantId: Long) {
        viewModelScope.launch {
            try {
                plantRepository.markPlantAsWatered(plantId)
                // The StateFlow from repository will automatically update our state
                // through the collect in loadPlants()
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = R.string.error_watering_plant
                )
            }
        }
    }
}
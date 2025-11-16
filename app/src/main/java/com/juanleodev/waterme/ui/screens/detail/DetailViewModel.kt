package com.juanleodev.waterme.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanleodev.waterme.R
import com.juanleodev.waterme.data.PlantRepository
import com.juanleodev.waterme.data.db.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DetailViewModel(
    private val repository: PlantRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()
    
    fun resetForNewPlant() {
        _state.value = DetailState()
    }

    fun handleIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadPlant -> loadPlant(intent.plantId)
            is DetailIntent.UpdateName -> updateName(intent.name)
            is DetailIntent.UpdateFrequency -> updateFrequency(intent.days)
            is DetailIntent.UpdateTime -> updateTime(intent.time)
            is DetailIntent.SelectPhoto -> selectPhoto(intent.photoUri)
            DetailIntent.SavePlant -> savePlant()
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name, nameError = null) }
    }

    private fun updateFrequency(days: Int) {
        _state.update { it.copy(frequency = days, frequencyError = null) }
    }

    private fun updateTime(time: java.time.LocalTime) {
        _state.update { it.copy(reminderTime = time) }
    }

    private fun selectPhoto(photoUri: String?) {
        _state.update { it.copy(photoUri = photoUri) }
    }

    private fun loadPlant(plantId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val plant = repository.getPlantById(plantId)
                if (plant != null) {
                    val lastWateringText = getLastWateringText(plant.lastWateringDate)
                    
                    _state.update {
                        it.copy(
                            plantId = plant.id,
                            name = plant.name,
                            photoUri = plant.photoUri,
                            frequency = plant.wateringFrequencyDays,
                            reminderTime = plant.reminderTime,
                            lastWateringDate = plant.lastWateringDate,
                            lastWateringText = lastWateringText,
                            isEditMode = true,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        nameError = R.string.error_loading_plants
                    )
                }
            }
        }
    }

    private fun getLastWateringText(lastWateringDate: java.time.LocalDate): String {
        val daysSince = java.time.temporal.ChronoUnit.DAYS.between(lastWateringDate, java.time.LocalDate.now()).toInt()
        return when (daysSince) {
            0 -> "Hoy"
            1 -> "Ayer"
            else -> "Hace $daysSince días"
        }
    }

    private fun savePlant() {
        // Validate inputs
        val currentState = _state.value
        val nameError = if (currentState.name.isBlank()) {
            R.string.error_name_required
        } else null
        
        val frequencyError = when {
            currentState.frequency < 1 -> R.string.error_frequency_min_days
            currentState.frequency > 90 -> R.string.error_frequency_max_days
            else -> null
        }

        // Update state with errors
        if (nameError != null || frequencyError != null) {
            _state.update {
                it.copy(
                    nameError = nameError,
                    frequencyError = frequencyError
                )
            }
            return
        }

        // If validation passes, save the plant
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                if (currentState.isEditMode) {
                    // Update existing plant
                    val plant = Plant(
                        id = currentState.plantId,
                        name = currentState.name.trim(),
                        photoUri = currentState.photoUri,
                        wateringFrequencyDays = currentState.frequency,
                        reminderTime = currentState.reminderTime,
                        lastWateringDate = currentState.lastWateringDate
                    )
                    
                    repository.updatePlant(plant)
                } else {
                    // Insert new plant
                    // For new plants, default to today as last watering date
                    val lastWateringDate = LocalDate.now()
                    
                    val plant = Plant(
                        name = currentState.name.trim(),
                        photoUri = currentState.photoUri,
                        wateringFrequencyDays = currentState.frequency,
                        reminderTime = currentState.reminderTime,
                        lastWateringDate = lastWateringDate
                    )
                    
                    repository.insertPlant(plant)
                }
                
                _state.update { 
                    it.copy(
                        isLoading = false,
                        isSaved = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        nameError = R.string.error_saving_plant,
                        errorArgs = listOf(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }
}

package com.juanleodev.waterme.ui.screens.list

import androidx.annotation.StringRes

/**
 * UI State for PlantListScreen.
 */
data class PlantListUiState(
    val isLoading: Boolean = false,
    val plants: List<PlantWithStatus> = emptyList(),
    @param:StringRes val error: Int? = null
)
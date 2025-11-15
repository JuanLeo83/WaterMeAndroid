package com.juanleodev.waterme.ui.screens.detail

import java.time.LocalTime

sealed interface DetailIntent {
    data class UpdateName(val name: String) : DetailIntent
    data class UpdateFrequency(val days: Int) : DetailIntent
    data class UpdateTime(val time: LocalTime) : DetailIntent
    data class UpdateLastWatering(val days: String) : DetailIntent
    data class SelectPhoto(val photoUri: String?) : DetailIntent
    data object SavePlant : DetailIntent
}

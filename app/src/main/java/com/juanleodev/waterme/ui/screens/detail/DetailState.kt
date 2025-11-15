package com.juanleodev.waterme.ui.screens.detail

import androidx.annotation.StringRes
import java.time.LocalTime

data class DetailState(
    val name: String = "",
    val photoUri: String? = null,
    val frequency: Int = 7,
    val reminderTime: LocalTime = LocalTime.of(9, 0),
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val frequencyError: Int? = null,
    val errorArgs: List<String>? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

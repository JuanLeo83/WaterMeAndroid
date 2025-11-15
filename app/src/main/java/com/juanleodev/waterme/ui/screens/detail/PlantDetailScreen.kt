package com.juanleodev.waterme.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanleodev.waterme.R
import com.juanleodev.waterme.ui.components.FrequencySlider
import com.juanleodev.waterme.ui.components.PhotoPicker
import com.juanleodev.waterme.ui.components.TimePickerButton
import com.juanleodev.waterme.ui.theme.WaterMeTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalTime

/**
 * Stateful screen for adding/editing plant details.
 * Follows MVI pattern with DetailViewModel.
 * 
 * @param plantId Plant ID for editing, -1L for new plant
 * @param onNavigateBack Callback when user navigates back
 * @param viewModel ViewModel injected via Koin
 */
@Composable
fun PlantDetailScreen(
    plantId: Long = -1L,
    onNavigateBack: () -> Unit = {},
    viewModel: DetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // Reset ViewModel state for new plants
    LaunchedEffect(plantId) {
        if (plantId == -1L) {
            viewModel.resetForNewPlant()
        }
    }
    
    // Navigate back when plant is saved successfully
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }
    
    PlantDetailContent(
        state = state,
        onIntent = viewModel::handleIntent
    )
}

/**
 * Stateless content composable for plant detail form.
 */
@Composable
private fun PlantDetailContent(
    state: DetailState,
    onIntent: (DetailIntent) -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Add New Plant", // TODO: Change to "Edit Plant" when editing
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Plant Name Field
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onIntent(DetailIntent.UpdateName(it)) },
                    label = { Text(stringResource(R.string.plant_name_label)) },
                    placeholder = { Text(stringResource(R.string.plant_name_hint)) },
                    singleLine = true,
                    isError = state.nameError != null,
                    supportingText = {
                        state.nameError?.let { errorRes ->
                            Text(
                                text = if (state.errorArgs != null) {
                                    stringResource(errorRes, *state.errorArgs.toTypedArray())
                                } else {
                                    stringResource(errorRes)
                                },
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                )
                
                // Photo Picker
                PhotoPicker(
                    photoUri = state.photoUri,
                    onPhotoSelected = { onIntent(DetailIntent.SelectPhoto(it)) }
                )
                
                // Frequency Slider
                FrequencySlider(
                    frequency = state.frequency,
                    onFrequencyChange = { onIntent(DetailIntent.UpdateFrequency(it)) }
                )
                
                // Frequency Error (if any)
                state.frequencyError?.let { errorRes ->
                    Text(
                        text = stringResource(errorRes),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                // Reminder Time
                TimePickerButton(
                    selectedTime = state.reminderTime,
                    onTimeSelected = { onIntent(DetailIntent.UpdateTime(it)) }
                )
                
                // TODO: Last Watering Field will be shown only in edit mode
                // For now, we'll use default value of "0" (today) for new plants
                // In edit mode, this will show: "Last watering: today/yesterday/X days ago/never"
                
                // Save Button
                Button(
                    onClick = { onIntent(DetailIntent.SavePlant) },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(stringResource(R.string.save_plant))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantDetailContentPreview() {
    WaterMeTheme {
        PlantDetailContent(
            state = DetailState(
                name = "My Fiddle Leaf Fig",
                frequency = 7,
                reminderTime = LocalTime.of(9, 0)
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantDetailContentErrorPreview() {
    WaterMeTheme {
        PlantDetailContent(
            state = DetailState(
                name = "",
                nameError = R.string.error_name_required
            ),
            onIntent = {}
        )
    }
}
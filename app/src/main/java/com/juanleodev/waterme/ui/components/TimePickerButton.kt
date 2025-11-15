package com.juanleodev.waterme.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanleodev.waterme.R
import com.juanleodev.waterme.ui.theme.WaterMeTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Enhanced TimePickerButton component using Material3 native TimePicker.
 * 
 * @param selectedTime Current selected time
 * @param onTimeSelected Callback when time is selected
 * @param modifier Optional modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerButton(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Initialize TimePicker state with current selected time
    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime.hour,
        initialMinute = selectedTime.minute,
        is24Hour = true
    )
    
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.reminder_time_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(
            onClick = { showTimePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🕘",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                )
            }
        }
        
        // Native Material3 TimePicker Dialog
        if (showTimePicker) {
            TimePickerDialog(
                onCancel = { showTimePicker = false },
                onConfirm = {
                    onTimeSelected(
                        LocalTime.of(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                    )
                    showTimePicker = false
                }
            ) {
                TimePicker(
                    state = timePickerState,
                )
            }
        }
    }
}

/**
 * Custom TimePickerDialog since Material3 might not have it in all versions
 */
@Composable
private fun TimePickerDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onCancel,
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.ok))
            }
        },
        text = { content() }
    )
}

@Preview(showBackground = true)
@Composable
private fun TimePickerButtonPreview() {
    WaterMeTheme {
        TimePickerButton(
            selectedTime = LocalTime.of(9, 0),
            onTimeSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
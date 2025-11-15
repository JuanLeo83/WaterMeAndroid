package com.juanleodev.waterme.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
 * Dumb component for selecting reminder time.
 * 
 * @param selectedTime Current selected time
 * @param onTimeSelected Callback when time is selected
 * @param modifier Optional modifier
 */
@Composable
fun TimePickerButton(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }
    
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
        
        if (showTimePicker) {
            // TODO: In a real app, this would show a proper time picker dialog
            // For now, we'll use a simple implementation
            TimePickerDialog(
                initialTime = selectedTime,
                onTimeSelected = { time ->
                    onTimeSelected(time)
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
    }
}

/**
 * Simple time picker dialog implementation.
 * In a production app, this could use Material3's TimePicker component.
 */
@Composable
private fun TimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    // For MVP, we'll provide some preset times
    val presetTimes = listOf(
        LocalTime.of(7, 0),
        LocalTime.of(8, 0),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        LocalTime.of(12, 0),
        LocalTime.of(18, 0),
        LocalTime.of(20, 0)
    )
    
    // Simple dialog implementation - in production, use AlertDialog
    Box {
        Column {
            Text(stringResource(R.string.select_reminder_time))
            presetTimes.forEach { time ->
                Button(
                    onClick = { onTimeSelected(time) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(time.format(DateTimeFormatter.ofPattern("HH:mm")))
                }
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
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
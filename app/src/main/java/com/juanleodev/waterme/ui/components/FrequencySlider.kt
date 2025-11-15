package com.juanleodev.waterme.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanleodev.waterme.R
import com.juanleodev.waterme.ui.theme.WaterMeTheme

/**
 * Dumb component for selecting watering frequency (1-90 days).
 * 
 * @param frequency Current frequency value (1-90)
 * @param onFrequencyChange Callback when frequency changes
 * @param modifier Optional modifier
 */
@Composable
fun FrequencySlider(
    frequency: Int,
    onFrequencyChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.frequency_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = pluralStringResource(
                    R.plurals.days_count, 
                    frequency, 
                    frequency
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Slider(
            value = frequency.toFloat(),
            onValueChange = { onFrequencyChange(it.toInt()) },
            valueRange = 1f..90f,
            steps = 88, // 90 - 1 - 1 = 88 steps between min and max
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequencySliderPreview() {
    WaterMeTheme {
        FrequencySlider(
            frequency = 7,
            onFrequencyChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
package com.juanleodev.waterme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.juanleodev.waterme.R
import com.juanleodev.waterme.ui.theme.WaterMeTheme

/**
 * Dumb component for selecting a photo for the plant.
 * 
 * @param photoUri Current photo URI, null if no photo selected
 * @param onPhotoSelected Callback when photo is selected
 * @param modifier Optional modifier
 */
@Composable
fun PhotoPicker(
    photoUri: String?,
    onPhotoSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.photo_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { 
                    // TODO: In a real app, this would open image picker
                    // For MVP, we'll just clear/set a placeholder
                    onPhotoSelected(if (photoUri == null) "placeholder" else null)
                },
            contentAlignment = Alignment.Center
        ) {
            if (photoUri != null && photoUri != "placeholder") {
                // TODO: In production, use AsyncImage from Coil to load actual images
                // For now, show placeholder text
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "📷",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = "Image: $photoUri",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (photoUri == "placeholder") {
                // Show a placeholder when "selected"
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "📷",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = stringResource(R.string.photo_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Show add photo prompt
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.add_photo_optional),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoPickerPreview() {
    WaterMeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PhotoPicker(
                photoUri = null,
                onPhotoSelected = {},
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            PhotoPicker(
                photoUri = "placeholder",
                onPhotoSelected = {},
            )
        }
    }
}
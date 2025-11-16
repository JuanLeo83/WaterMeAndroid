package com.juanleodev.waterme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanleodev.waterme.R
import com.juanleodev.waterme.data.db.Plant
import com.juanleodev.waterme.data.model.WateringStatus
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Reusable component for displaying a plant in a list.
 * 
 * Displays plant information including name, photo, watering status,
 * and provides a water button for user interaction.
 */
@Composable
fun PlantListItem(
    plant: Plant,
    status: WateringStatus,
    onWaterClick: (Long) -> Unit,
    onClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick(plant.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Plant photo
            PlantPhoto(
                photoUri = plant.photoUri,
                contentDescription = "Photo of ${plant.name}",
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Plant info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Plant name
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Status indicator with text
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WateringStatusIndicator(
                        status = status,
                        modifier = Modifier.size(12.dp)
                    )
                    
                    Text(
                        text = stringResource(status.displayNameRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Last watering info
                Text(
                    text = stringResource(R.string.last_watered, formatDate(plant.lastWateringDate)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Next watering info
                Text(
                    text = stringResource(R.string.next_watering, formatDate(plant.getNextWateringDate())),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Water button
            FilledTonalButton(
                onClick = { onWaterClick(plant.id) },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFlorist,
                    contentDescription = stringResource(R.string.water_plant),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.water_plant))
            }
        }
    }
}

/**
 * Plant photo component with default placeholder.
 */
@Composable
private fun PlantPhoto(
    photoUri: String?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (photoUri.isNullOrBlank()) {
            // Default plant icon
            Icon(
                imageVector = Icons.Default.LocalFlorist,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUri)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * Circular indicator showing watering status color.
 */
@Composable
private fun WateringStatusIndicator(
    status: WateringStatus,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(status.color)
    )
}

/**
 * Format a LocalDate to a readable string.
 */
private fun formatDate(date: java.time.LocalDate): String {
    return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
}
package com.juanleodev.waterme.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.juanleodev.waterme.R

/**
 * State of the notification permission banner.
 */
enum class NotificationPermissionState {
    /** Permission not requested yet - show friendly banner with dismiss option */
    NOT_REQUESTED,
    /** Permission denied - show warning banner without dismiss option */
    DENIED
}

/**
 * Banner that appears at the top of the plant list to request notification permissions.
 * Shows different styles based on permission state.
 */
@Composable
fun NotificationPermissionBanner(
    visible: Boolean,
    state: NotificationPermissionState,
    onActionClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(),
        exit = slideOutVertically(),
        modifier = modifier
    ) {
        val (backgroundColor, contentColor, icon, title, message) = when (state) {
            NotificationPermissionState.NOT_REQUESTED -> BannerStyle(
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.notification_permission_title),
                message = stringResource(R.string.notification_permission_message)
            )
            NotificationPermissionState.DENIED -> BannerStyle(
                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                icon = Icons.Default.NotificationsOff,
                title = stringResource(R.string.notification_permission_denied_title),
                message = stringResource(R.string.notification_permission_denied_message)
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = contentColor
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                // Only show dismiss button for NOT_REQUESTED state
                if (state == NotificationPermissionState.NOT_REQUESTED) {
                    TextButton(onClick = onDismissClick) {
                        Text(
                            text = stringResource(R.string.notification_permission_dismiss),
                            color = contentColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Button(onClick = onActionClick) {
                    Text(text = stringResource(R.string.notification_permission_enable))
                }
            }
        }
    }
}

/**
 * Data class to hold banner style properties.
 */
private data class BannerStyle(
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val contentColor: androidx.compose.ui.graphics.Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val message: String
)

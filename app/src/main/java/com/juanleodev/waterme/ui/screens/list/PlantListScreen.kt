package com.juanleodev.waterme.ui.screens.list

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.juanleodev.waterme.R
import com.juanleodev.waterme.ui.components.NotificationPermissionBanner
import com.juanleodev.waterme.ui.components.NotificationPermissionState
import com.juanleodev.waterme.ui.components.PlantListItem
import org.koin.androidx.compose.koinViewModel

/**
 * Plant List Screen - Main screen showing all plants with their watering status.
 * 
 * Features:
 * - Displays plants in a scrollable list
 * - Shows watering status indicators
 * - Allows watering plants with a button
 * - Provides navigation to add new plants
 * - Handles loading and error states
 * - Requests notification permissions if not granted
 * - Shows permanent warning banner if permissions are denied
 * 
 * Following MVI pattern with PlantListViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantListScreen(
    onAddPlantClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Track notification permission state
    var hasNotificationPermission by remember { mutableStateOf(false) }
    var hasUserDismissedBanner by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }
    
    // Function to check notification permission
    fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PermissionChecker.PERMISSION_GRANTED
        } else {
            true // Permissions not needed for Android < 13
        }
    }
    
    // Check permission on first composition
    LaunchedEffect(Unit) {
        hasNotificationPermission = checkNotificationPermission()
    }
    
    // Re-check permission when returning from settings
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasNotificationPermission = checkNotificationPermission()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (!isGranted) {
            // Permission was denied - mark as permanently denied for UI purposes
            permissionDeniedPermanently = true
        }
    }
    
    // Function to handle permission request or open settings
    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (permissionDeniedPermanently) {
                // Open app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            } else {
                // Try to request permission
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    
    // Determine banner state
    val showBanner = !hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val bannerState = when {
        !hasNotificationPermission && permissionDeniedPermanently -> NotificationPermissionState.DENIED
        !hasNotificationPermission && hasUserDismissedBanner -> NotificationPermissionState.DENIED
        !hasNotificationPermission && !hasUserDismissedBanner -> NotificationPermissionState.NOT_REQUESTED
        else -> null
    }
    
    // Handle error messages
    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorResId ->
            val errorMessage = context.getString(errorResId)
            snackbarHostState.showSnackbar(errorMessage)
        }
    }
    
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlantClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_new_plant)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Notification permission banner
            bannerState?.let { state ->
                NotificationPermissionBanner(
                    visible = showBanner,
                    state = state,
                    onActionClick = {
                        requestNotificationPermission()
                    },
                    onDismissClick = {
                        hasUserDismissedBanner = true
                    }
                )
            }
            
            // Main content
            PlantListContent(
                uiState = uiState,
                onWaterPlant = { plantId ->
                    viewModel.handleEvent(PlantListEvent.WaterPlant(plantId))
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Content of the plant list screen.
 */
@Composable
private fun PlantListContent(
    uiState: PlantListUiState,
    onWaterPlant: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            LoadingContent(modifier = modifier)
        }
        
        uiState.plants.isEmpty() -> {
            EmptyContent(modifier = modifier)
        }
        
        else -> {
            PlantList(
                plants = uiState.plants,
                onWaterPlant = onWaterPlant,
                modifier = modifier
            )
        }
    }
}

/**
 * Loading state content.
 */
@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.loading_plants),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Empty state content.
 */
@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "🌱",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = stringResource(R.string.no_plants_yet),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.add_your_first_plant),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * List of plants content.
 */
@Composable
private fun PlantList(
    plants: List<PlantWithStatus>,
    onWaterPlant: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = plants,
            key = { plantWithStatus -> plantWithStatus.plant.id }
        ) { plantWithStatus ->
            PlantListItem(
                plant = plantWithStatus.plant,
                status = plantWithStatus.status,
                onWaterClick = onWaterPlant,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
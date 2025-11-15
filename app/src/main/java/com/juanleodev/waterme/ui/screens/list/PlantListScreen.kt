package com.juanleodev.waterme.ui.screens.list

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juanleodev.waterme.R
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
        PlantListContent(
            uiState = uiState,
            onWaterPlant = { plantId ->
                viewModel.handleEvent(PlantListEvent.WaterPlant(plantId))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
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
package com.juanleodev.waterme.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juanleodev.waterme.ui.screens.detail.PlantDetailScreen

/**
 * Main navigation graph for the WaterMe app.
 * Defines routes and navigation structure.
 */
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.PlantList.route
    ) {
        // Plant List Screen
        composable(route = Routes.PlantList.route) {
            // TODO: Implement PlantListScreen in Historia 2
            PlantListPlaceholder(
                onAddPlantClick = {
                    navController.navigate(Routes.PlantDetail.createRoute())
                }
            )
        }
        
        // Plant Detail Screen (for adding/editing)
        composable(
            route = Routes.PlantDetail.route,
            arguments = listOf(
                navArgument("plantId") {
                    type = NavType.LongType
                    defaultValue = -1L // -1 means "new plant"
                }
            )
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getLong("plantId") ?: -1L
            PlantDetailScreen(
                plantId = plantId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Temporary placeholder for Plant List screen.
 * Will be replaced with actual PlantListScreen in Historia 2.
 */
@Composable
private fun PlantListPlaceholder(onAddPlantClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Plant List Screen",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "No plants yet. Add your first plant!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Button(
                onClick = onAddPlantClick,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text("Add Plant")
            }
        }
    }
}

/**
 * Navigation routes for the app.
 */
sealed class Routes(val route: String) {
    data object PlantList : Routes("list")
    data object PlantDetail : Routes("detail/{plantId}") {
        fun createRoute(plantId: Long = -1L) = "detail/$plantId"
    }
}

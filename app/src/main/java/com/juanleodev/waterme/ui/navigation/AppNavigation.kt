package com.juanleodev.waterme.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

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
            // TODO: Implement PlantListScreen in Historia 1
            PlaceholderScreen(text = "Plant List Screen")
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
            // TODO: Implement PlantDetailScreen in Historia 1
            PlaceholderScreen(text = "Plant Detail Screen\nPlant ID: $plantId")
        }
    }
}

/**
 * Temporary placeholder screen for setup.
 * Will be replaced with actual screens in Historia 1.
 */
@Composable
private fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
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

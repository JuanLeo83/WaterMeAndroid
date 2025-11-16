package com.juanleodev.waterme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juanleodev.waterme.ui.screens.list.PlantListScreen
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
            PlantListScreen(
                onAddPlantClick = {
                    navController.navigate(Routes.PlantDetail.createRoute())
                },
                onPlantClick = { plantId ->
                    navController.navigate(Routes.PlantDetail.createRoute(plantId))
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
 * Navigation routes for the app.
 */
sealed class Routes(val route: String) {
    data object PlantList : Routes("list")
    data object PlantDetail : Routes("detail/{plantId}") {
        fun createRoute(plantId: Long = -1L) = "detail/$plantId"
    }
}

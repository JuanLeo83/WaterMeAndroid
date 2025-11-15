package com.juanleodev.waterme.di

import org.koin.dsl.module

/**
 * Main Koin module for dependency injection.
 * This module will be extended as we add more components (Database, Repository, ViewModels, etc.)
 */
val appModule = module {
    
    // Database module will be added here
    // Example: single { AppDatabase.getDatabase(androidContext()) }
    
    // Repository module will be added here
    // Example: single { PlantRepository(get()) }
    
    // ViewModel module will be added here
    // Example: viewModel { PlantListViewModel(get()) }
    
}

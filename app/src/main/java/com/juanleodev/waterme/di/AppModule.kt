package com.juanleodev.waterme.di

import com.juanleodev.waterme.data.PlantRepository
import com.juanleodev.waterme.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Main Koin module for dependency injection.
 * This module will be extended as we add more components (ViewModels, Workers, etc.)
 */
val appModule = module {
    
    // Database
    single { AppDatabase.getDatabase(androidContext()) }
    
    // DAOs
    single { get<AppDatabase>().plantDao() }
    
    // Repositories
    single { PlantRepository(get()) }
    
    // ViewModels will be added here
    // Example: viewModel { PlantListViewModel(get()) }
    
}

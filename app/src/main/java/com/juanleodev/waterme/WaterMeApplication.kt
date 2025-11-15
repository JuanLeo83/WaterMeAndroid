package com.juanleodev.waterme

import android.app.Application
import com.juanleodev.waterme.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WaterMeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@WaterMeApplication)
            modules(appModule)
        }
    }
}

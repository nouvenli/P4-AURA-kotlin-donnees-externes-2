package com.aura

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 *This is the main application
 * It uses Hilt for dependency injection.
 * It extends the Android Application class.
 * Generates the code necessary for dependency injection.
 */
@HiltAndroidApp
class AuraApplication : Application() {
}
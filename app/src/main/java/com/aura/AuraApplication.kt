package com.aura

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Classe utilisant Hilt pour l'injection de dépendances.
 * Elle étend la classe Application de Android.
 * génère le code nécessaire pour l'injection de dépendances
 */
@HiltAndroidApp
class AuraApplication : Application() {
}
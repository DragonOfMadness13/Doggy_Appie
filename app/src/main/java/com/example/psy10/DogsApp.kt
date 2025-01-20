package com.example.psy10

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// This sets up the Hilt dependency injection container for the application
// It also makes the DogsApp the Application class, which is required by Hilt
// And gives us access to the whole application global states
@HiltAndroidApp
class DogsApp: Application()
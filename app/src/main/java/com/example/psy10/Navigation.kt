package com.example.psy10

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp  // Dodaj ten import
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.psy10.ui.theme.dogs.DogsScreen
import com.example.psy10.ui.theme.details.DogDetailScreen
import com.example.psy10.ui.theme.profile.SettingsScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") {
                DogsScreen(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    navigationController = navController
                )
            }
            composable(
                route = "details/{dogId}",
                arguments = listOf(navArgument("dogId") { type = NavType.IntType })
            ) {
                DogDetailScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }
            composable("settings") {
                SettingsScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}
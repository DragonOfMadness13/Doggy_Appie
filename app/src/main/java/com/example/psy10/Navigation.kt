package com.example.psy10

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.psy10.ui.theme.dogs.DogsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                DogsScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    navigationController = navController
                )
            }
            composable("details") {
                Text("Here will be a doggo :)", modifier = Modifier.padding(innerPadding))
            }
            composable("settings") {
                Text("Check your Setting :)", modifier = Modifier.padding(innerPadding))
            }
            composable("profile") {
                Text("Check your Setting :)", modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

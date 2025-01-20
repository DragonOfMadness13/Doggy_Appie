package com.example.psy10.ui.theme.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.psy10.ui.theme.TopBar

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Ustawienia",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )
    }
}


package com.example.psy10.ui.theme.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.psy10.ui.theme.TopBar

@Composable
fun ProfileScreen(
    NavController: NavController
){
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Profil",
            canNavigateBack = true,
        )
    }
}
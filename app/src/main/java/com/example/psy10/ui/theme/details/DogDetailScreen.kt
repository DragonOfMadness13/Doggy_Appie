package com.example.psy10.ui.theme.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.psy10.ui.theme.TopBar

@Composable
fun DogDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: DogDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val dog by viewModel.dog.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = dog?.name ?: "Dog Details",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        dog?.let { dogData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = dogData.imageUrl,
                    contentDescription = "Dog image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Name: ${dogData.name}")
                    Text("Breed: ${dogData.breed}")
                    Text("Owner: ${dogData.ownerName}")
                    Text(if (dogData.isFav) "❤️ Favorite" else "🤍 Not favorite")
                }
            }
        }
    }
}
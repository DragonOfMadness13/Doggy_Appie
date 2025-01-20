package com.example.psy10.ui.theme.dogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.psy10.data.models.Dog
import android.util.Log

@Composable
fun DogsScreen(
    modifier: Modifier = Modifier,
    viewModel: DogsViewModel = hiltViewModel(),
    navigationController: NavController,
) {
    Log.d("DogsScreen", "DogsScreen composable started")
    val items by viewModel.uiState.collectAsStateWithLifecycle()

    when (items) {
        is UiState.Success -> {
            Log.d("DogsScreen", "Showing success state with ${(items as UiState.Success).data.size} dogs")
            DogListContent(
                items = (items as UiState.Success).data,
                onAdd = viewModel::addDog,
                onDeleteClick = viewModel::removeDog,
                onFavoriteClick = viewModel::triggerFav,
                onDogClick = { dogId -> navigationController.navigate("details/$dogId") },
                onSetClick = { navigationController.navigate("settings") },
                onProClick = { navigationController.navigate("profile") },
                modifier = modifier,
            )
        }
        is UiState.Loading -> {
            Log.d("DogsScreen", "Showing loading state")
            Text("Loading...")
        }
        is UiState.Error -> {
            Log.e("DogsScreen", "Showing error state", (items as UiState.Error).throwable)
            Text("Error: ${(items as UiState.Error).throwable.message}")
        }
    }
}

@Composable
fun DogListContent(
    items: List<Dog>,
    onFavoriteClick: (id: Int) -> Unit,
    onAdd: (name: String) -> Unit,
    onDeleteClick: (id: Int) -> Unit,
    onDogClick: (dogId: Int) -> Unit,
    onSetClick: () -> Unit,
    onProClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var searchQuery by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        var isSearching by remember { mutableStateOf(false) }

        val searchResult = remember(items, searchQuery, isSearching) {
            if (isSearching && searchQuery.isNotEmpty()) {
                items.filter { it.name.contains(searchQuery, ignoreCase = true) }
            } else {
                items
            }
        }

        val totaldogs = items.size
        val favoriteDogs = items.count { it.isFav }

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onSetClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.Black
                )
            }
            Text(
                text = "Doggos",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            IconButton(onClick = onProClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.Black
                )
            }
        }

        // Search and Add
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    isSearching = true
                },
                placeholder = { Text("Poszukaj lub dodaj pieska 🐕") },
                modifier = Modifier
                    .weight(1f)
                    .background(if (errorMessage.isNotEmpty()) Color.Red.copy(alpha = 0.1f) else Color.Transparent)
            )
            IconButton(
                onClick = { isSearching = true },
                enabled = searchQuery.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Dog"
                )
            }
            IconButton(
                onClick = {
                    Log.d("DogListContent", "Add button clicked with query: $searchQuery")
                    if (searchQuery.isBlank()) {
                        errorMessage = "Wprowadź imię pieska"
                    } else if (items.any { it.name.equals(searchQuery, ignoreCase = true) }) {
                        errorMessage = "Piesek jest już na liście"
                    } else {
                        Log.d("DogListContent", "Calling onAdd with name: $searchQuery")
                        onAdd(searchQuery)
                        searchQuery = ""
                        errorMessage = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Dog",
                    tint = Color.Black
                )
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Text(
            text = "🐕: $totaldogs    💜: $favoriteDogs",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResult.size) { index ->
                val dog = searchResult[index]
                DogItem(
                    dog = dog,
                    onFavoriteClick = { onFavoriteClick(dog.id) },
                    onDeleteClick = { onDeleteClick(dog.id) },
                    onDogClick = { onDogClick(dog.id) }
                )
            }
        }
    }
}

@Composable
fun DogItem(
    dog: Dog,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDogClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDogClick)
            .padding(8.dp)
    ) {
        // Dog Image
        AsyncImage(
            model = dog.imageUrl,
            contentDescription = "Dog image",
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = dog.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dog.breed,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        IconButton(onClick = onFavoriteClick) {
            Text(
                text = if (dog.isFav) "💜" else "🤍",
                fontSize = 20.sp
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                tint = Color.Red,
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
}

private fun bcgradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(Color(0xFF65558F), Color(0xFFEEB6E8))
    )
}
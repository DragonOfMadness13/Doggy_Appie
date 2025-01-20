package com.example.psy10.ui.theme.dogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.psy10.data.models.Dog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogsScreen(
    modifier: Modifier = Modifier,
    viewModel: DogsViewModel = hiltViewModel(),
    navigationController: NavController,
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is UiState.Success) {
        DogListItem(
            items = (items as UiState.Success).data,
            onAdd = viewModel::addDog,
            onDeleteClick = viewModel::removeDog,
            onFavoriteClick = viewModel::triggerFav,
            onDogClick = { navigationController.navigate("details") },
            onSetClick = { navigationController.navigate("settings") },
            onProClick = { navigationController.navigate("profile") },
            modifier = modifier,
        )
    }
}


@Composable
fun DogListItem(
    items: List<Dog>,
    onFavoriteClick: (id: Int) -> Unit,
    onAdd: (name: String) -> Unit,
    onDeleteClick: (id: Int) -> Unit,
    onDogClick: () -> Unit,
    onSetClick: () -> Unit,
    onProClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var searchQuery by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        var isSearching by remember { mutableStateOf(false) }
        var searchResult= remember(items, searchQuery, isSearching) {
            if (isSearching && searchQuery.isNotEmpty()) {
                items.filter { it.name.contains(searchQuery, ignoreCase = true) }
            } else {
                items
            }
        }

        var totaldogs = items.size
        var favoriteDogs = items.count { it.isFav }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onSetClick() }) {
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
            IconButton(onClick = { onProClick() }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.Black
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    isSearching = false
                                },
                placeholder = { Text("Poszukaj lub dodaj pieska 🐕") },
                modifier = Modifier
                    .weight(1f)
                    .background(if (errorMessage.isNotEmpty()) Color.Red.copy(alpha = 0.1f) else Color.Transparent)
            )
            IconButton(
                onClick = {searchResult},
                enabled = searchQuery.isNotEmpty(),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Search,
                    contentDescription = "Search Dog"
                )
            }
            IconButton(
                onClick = {
                    if (items.any { it.name.equals(searchQuery, ignoreCase = true) }) {
                        errorMessage = "Piesek jest już na liście"
                    } else {
                        onAdd(searchQuery)
                        searchQuery = ""
                        errorMessage = ""
                    }
                }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
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

        items.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.clickable {
                    onDogClick()
                }
            ) {
                Text(
                    "🐕", modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = bcgradient())
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(it.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(it.breed, fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(Modifier.weight(1f))

                IconButton(onClick = { onFavoriteClick(it.id) }) {
                    Text(
                        text = if (it.isFav) "💜" else "🤍",
                        fontSize = 20.sp
                    )
                }
                IconButton(onClick = { onDeleteClick(it.id) }) {
                    Icon(
                        tint = Color.Red,
                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}
private fun bcgradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(Color(0xFF65558F), Color(0xFFEEB6E8))
    )
}
//@Preview(showBackground = true)
//@Composable
//fun PreviewDogListApp() {
//    DogListApp(navController: NavController)
//}
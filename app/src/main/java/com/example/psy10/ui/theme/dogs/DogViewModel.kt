package com.example.psy10.ui.theme.dogs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psy10.data.DogRepository
import com.example.psy10.data.models.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogsViewModel @Inject constructor(
    private val dogRepository: DogRepository
) : ViewModel() {

    init {
        Log.d("DogsViewModel", "ViewModel initialized")
    }

    val uiState: StateFlow<UiState> = dogRepository
        .dogs
        .map<List<Dog>, UiState> {
            Log.d("DogsViewModel", "Received ${it.size} dogs from repository")
            UiState.Success(data = it)
        }
        .catch {
            Log.e("DogsViewModel", "Error in dogs flow", it)
            emit(UiState.Error(it))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading) // Zmienione na UiState.Loading

    fun addDog(name: String) {
        Log.d("DogsViewModel", "Adding dog: $name")
        if (name.isBlank()) {
            Log.w("DogsViewModel", "Attempted to add dog with blank name")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("DogsViewModel", "Calling repository.add() for dog: $name")
                dogRepository.add(name)
                Log.d("DogsViewModel", "Successfully added dog: $name")
            } catch (e: Exception) {
                Log.e("DogsViewModel", "Error adding dog: $name", e)
            }
        }
    }

    fun removeDog(id: Int) {
        Log.d("DogsViewModel", "Removing dog with id: $id")
        viewModelScope.launch {
            try {
                dogRepository.remove(id)
                Log.d("DogsViewModel", "Successfully removed dog with id: $id")
            } catch (e: Exception) {
                Log.e("DogsViewModel", "Error removing dog with id: $id", e)
            }
        }
    }

    fun triggerFav(id: Int) {
        Log.d("DogsViewModel", "Toggling favorite for dog with id: $id")
        viewModelScope.launch {
            try {
                dogRepository.triggerFav(id)
                Log.d("DogsViewModel", "Successfully toggled favorite for dog with id: $id")
            } catch (e: Exception) {
                Log.e("DogsViewModel", "Error toggling favorite for dog with id: $id", e)
            }
        }
    }
}

sealed interface UiState {
    object Loading: UiState
    data class Error(val throwable: Throwable): UiState
    data class Success(val data: List<Dog>): UiState
}
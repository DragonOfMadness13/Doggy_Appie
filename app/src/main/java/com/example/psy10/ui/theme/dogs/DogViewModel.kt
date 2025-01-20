package com.example.psy10.ui.theme.dogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psy10.data.DogRepository
import com.example.psy10.data.models.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.example.psy10.ui.theme.dogs.UiState.Success
import com.example.psy10.ui.theme.dogs.UiState.Loading
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.emptyFlow

@HiltViewModel
class DogsViewModel @Inject constructor(
    private val dogRepository: DogRepository
) : ViewModel() {

    val uiState: StateFlow<UiState> = dogRepository
        .dogs
        .map<List<Dog>, UiState> { Success(data = it) }
        .catch { emit(UiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addDog(name: String) {
        viewModelScope.launch {
            dogRepository.add(name)
        }
    }

    fun removeDog(id: Int) {
        viewModelScope.launch {
            dogRepository.remove(id)
        }
    }

    fun triggerFav(id: Int) {
        viewModelScope.launch {
            dogRepository.triggerFav(id)
        }
    }
}

sealed interface UiState {
    object Loading: UiState
    data class Error(val throwable: Throwable): UiState
    data class Success(val data: List<Dog>): UiState
}
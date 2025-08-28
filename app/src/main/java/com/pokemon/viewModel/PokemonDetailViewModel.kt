package com.pokemon.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokemon.model.Pokemon
import com.pokemon.model.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(private val repository: PokemonRepository) : ViewModel() {

    // State for pokemon details
    private val _pokemon = MutableStateFlow<Pokemon?>(null)
    val pokemon: StateFlow<Pokemon?> = _pokemon.asStateFlow()

    // State for loading indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State for error handling
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadPokemon(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.getPokemon(id).collect { pokemon ->
                    _pokemon.value = pokemon
                }
            } catch (e: Exception) {
                _error.value = "Failed to load Pokemon details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Clear error message
    fun clearError() {
        _error.value = null
    }
}

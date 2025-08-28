package com.pokemon.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokemon.model.PokemonBasic
import com.pokemon.model.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {

    // State for pokemon list
    private val _pokemonList = MutableStateFlow<List<PokemonBasic>>(emptyList())
    val pokemonList: StateFlow<List<PokemonBasic>> = _pokemonList.asStateFlow()

    // State for loading indicator (for pagination)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State for refresh indicator
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // State for error handling
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Pagination variables
    private var currentOffset = 0
    private var isLastPage = false

    init {
        loadPokemon() // Load initial data when ViewModel is created
    }

    // Load more pokemon (for pagination)
    fun loadPokemon() {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.getPokemonList(currentOffset).collect { response ->
                    // Add new pokemon to existing list
                    _pokemonList.value = _pokemonList.value + response.results
                    currentOffset += 20
                    isLastPage = response.next == null
                }
            } catch (e: Exception) {
                _error.value = "Failed to load Pokemon: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Refresh the entire list (pull-to-refresh)
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null

            try {
                // Reset pagination
                currentOffset = 0
                isLastPage = false

                repository.getPokemonList(0).collect { response ->
                    // Replace entire list with fresh data
                    _pokemonList.value = response.results
                    currentOffset = 20
                    isLastPage = response.next == null
                }
            } catch (e: Exception) {
                _error.value = "Failed to refresh: ${e.message}"
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    // Clear error message
    fun clearError() {
        _error.value = null
    }
}

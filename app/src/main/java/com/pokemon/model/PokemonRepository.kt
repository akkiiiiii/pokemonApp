package com.pokemon.model


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepository(private val api: PokemonApi) {

    fun getPokemonList(offset: Int): Flow<PokemonListResponse> = flow {
        try {
            val response = api.getPokemonList(offset = offset)
            emit(response)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getPokemon(id: Int): Flow<Pokemon> = flow {
        try {
            val pokemon = api.getPokemon(id)
            emit(pokemon)
        } catch (e: Exception) {
            throw e
        }
    }
}

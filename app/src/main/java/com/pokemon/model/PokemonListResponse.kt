package com.pokemon.model


data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonBasic>
)

data class PokemonBasic(
    val name: String,
    val url: String
) {
    // This Line will extract the pokemon id from the url
    val id: Int
        get() = url.split("/").dropLast(1).last().toInt()
}

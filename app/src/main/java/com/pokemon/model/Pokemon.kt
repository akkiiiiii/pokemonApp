package com.pokemon.model


data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val stats: List<Stat>,
    val abilities: List<Ability>,
    val types: List<Type>
)

data class Sprites(
    val front_default: String?,
    val other: Other?
)

data class Other(
    val `official-artwork`: OfficialArtwork?
)

data class OfficialArtwork(
    val front_default: String?
)

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatDetail
)

data class StatDetail(
    val name: String,
    val url: String
)

data class Ability(
    val ability: AbilityDetail,
    val is_hidden: Boolean,
    val slot: Int
)

data class AbilityDetail(
    val name: String,
    val url: String
)

data class Type(
    val slot: Int,
    val type: TypeDetail
)

data class TypeDetail(
    val name: String,
    val url: String
)

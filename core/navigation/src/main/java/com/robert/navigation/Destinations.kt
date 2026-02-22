package com.robert.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {
    @Serializable
    data object Characters : Destinations

    @Serializable
    data class CharacterDetails(val characterId: Int) : Destinations
}

package com.robert.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object CharacterList : Destination

    @Serializable
    data class CharacterDetails(val characterId: Int) : Destination
}

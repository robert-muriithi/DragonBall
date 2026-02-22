package com.robert.character_details.model

import androidx.compose.runtime.Immutable


@Immutable
data class PlanetModel(
    val id: Int,
    val name: String,
    val isDestroyed: Boolean?,
    val description: String?,
    val image: String?
)


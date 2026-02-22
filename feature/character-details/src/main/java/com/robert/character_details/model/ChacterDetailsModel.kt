package com.robert.character_details.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class CharacterDetailsModel(
    val id: Int,
    val name: String,
    val race: String?,
    val gender: String?,
    val affiliation: String?,
    val description: String?,
    val image: String?,
    val ki: String?,
    val maxKi: String?,
    val status: String?,
    val originPlanet: PlanetModel?,
    val transformations: ImmutableList<TransformationModel>
)
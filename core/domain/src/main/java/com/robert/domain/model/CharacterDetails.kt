package com.robert.domain.model

import kotlinx.collections.immutable.ImmutableList

data class CharacterDetails(
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
    val originPlanet: Planet?,
    val transformations: ImmutableList<Transformation>
)


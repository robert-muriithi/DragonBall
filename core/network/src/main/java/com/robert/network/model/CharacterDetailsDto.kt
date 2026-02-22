package com.robert.network.model

data class CharacterDetailsDto(
    val id: Int,
    val name: String,
    val race: String?,
    val gender: String?,
    val affiliation: String?,
    val image: String?,
    val ki: String?,
    val maxKi: String?,
    val description: String?,
    val deletedAt: String?,
    val originPlanet: PlanetDto?,
    val transformations: List<TransformationDto>
)


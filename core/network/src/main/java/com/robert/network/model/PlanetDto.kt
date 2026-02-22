package com.robert.network.model

data class PlanetDto(
    val id: Int,
    val name: String,
    val isDestroyed: Boolean?,
    val description: String?,
    val image: String?
)


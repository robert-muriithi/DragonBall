package com.robert.domain.model

data class Planet(
    val id: Int,
    val name: String,
    val isDestroyed: Boolean?,
    val description: String?,
    val image: String?
)


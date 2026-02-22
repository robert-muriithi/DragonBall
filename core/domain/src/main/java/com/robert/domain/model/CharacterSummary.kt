package com.robert.domain.model

data class CharacterSummary(
    val id: Int,
    val name: String,
    val race: String?,
    val gender: String?,
    val affiliation: String?,
    val image: String?,
    val ki: String?,
    val maxKi: String?
)


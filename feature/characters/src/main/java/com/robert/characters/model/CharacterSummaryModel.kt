package com.robert.characters.model

import androidx.compose.runtime.Immutable

@Immutable
data class CharacterSummaryModel(
    val id: Int,
    val name: String,
    val race: String?,
    val gender: String?,
    val affiliation: String?,
    val image: String?,
    val ki: String?,
    val maxKi: String?
)
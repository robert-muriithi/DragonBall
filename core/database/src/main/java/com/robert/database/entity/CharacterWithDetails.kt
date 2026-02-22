package com.robert.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CharacterWithDetails(
    @Embedded val character: CharacterEntity,

    @Relation(parentColumn = "id", entityColumn = "characterId")
    val extras: CharacterDetailsEntity?,

    @Relation(parentColumn = "id", entityColumn = "characterId")
    val planet: PlanetEntity?,

    @Relation(parentColumn = "id", entityColumn = "characterId")
    val transformations: List<TransformationEntity>
)

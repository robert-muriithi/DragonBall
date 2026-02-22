package com.robert.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robert.database.DatabaseConstants.Tables.CHARACTER_ENTITY

@Entity(tableName = CHARACTER_ENTITY)
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val race: String?,
    val gender: String?,
    val affiliation: String?,
    val image: String?,
    val ki: String?,
    val maxKi: String?
)

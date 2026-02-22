package com.robert.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.robert.database.DatabaseConstants.Tables.CHARACTER_DETAILS_ENTITY
import com.robert.database.DatabaseConstants.Tables.CHARACTER_ENTITY

@Entity(
    tableName = CHARACTER_DETAILS_ENTITY,
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CharacterDetailsEntity(
    @PrimaryKey val characterId: Int,
    val description: String?,
    val deletedAt: String?
)

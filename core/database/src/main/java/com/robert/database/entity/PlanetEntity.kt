package com.robert.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.robert.database.DatabaseConstants.Tables.CHARACTER_ENTITY
import com.robert.database.DatabaseConstants.Tables.PLANET_ENTITY

@Entity(
    tableName = PLANET_ENTITY,
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlanetEntity(
    @PrimaryKey val characterId: Int,
    val planetId: Int,
    val name: String,
    val isDestroyed: Boolean?,
    val description: String?,
    val image: String?
)

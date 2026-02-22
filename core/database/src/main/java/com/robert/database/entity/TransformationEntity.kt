package com.robert.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.robert.database.DatabaseConstants.Tables.CHARACTER_ENTITY
import com.robert.database.DatabaseConstants.Tables.TRANSFORMATION_ENTITY

@Entity(
    tableName = TRANSFORMATION_ENTITY,
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransformationEntity(
    @PrimaryKey val id: Int,
    val characterId: Int,
    val name: String,
    val image: String?,
    val ki: String?
)

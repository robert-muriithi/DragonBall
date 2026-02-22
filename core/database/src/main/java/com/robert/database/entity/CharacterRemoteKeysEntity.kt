package com.robert.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robert.database.DatabaseConstants.Tables.CHARACTER_REMOTE_KEYS_TABLE

@Entity(tableName = CHARACTER_REMOTE_KEYS_TABLE)
data class CharacterRemoteKeysEntity(
    @PrimaryKey val characterId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)

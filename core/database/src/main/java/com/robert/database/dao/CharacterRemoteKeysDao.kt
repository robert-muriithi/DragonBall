package com.robert.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robert.database.entity.CharacterRemoteKeysEntity

@Dao
interface CharacterRemoteKeysDao {

    @Query("SELECT * FROM characters_remote_keys WHERE characterId = :characterId")
    suspend fun remoteKeysByCharacterId(characterId: Int): CharacterRemoteKeysEntity?

    @Query("SELECT MAX(nextKey) FROM characters_remote_keys")
    suspend fun getNextPage(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(keys: List<CharacterRemoteKeysEntity>)

    @Query("DELETE FROM characters_remote_keys")
    suspend fun clearAll()
}

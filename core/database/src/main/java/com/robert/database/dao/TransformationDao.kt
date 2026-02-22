package com.robert.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robert.database.entity.TransformationEntity

@Dao
interface TransformationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<TransformationEntity>)

    @Query("DELETE FROM character_transformations WHERE characterId = :characterId")
    suspend fun deleteByCharacterId(characterId: Int)
}

package com.robert.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.robert.database.entity.CharacterDetailsEntity

@Dao
interface CharacterDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CharacterDetailsEntity)
}

package com.robert.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.robert.database.entity.PlanetEntity

@Dao
interface PlanetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: PlanetEntity)
}

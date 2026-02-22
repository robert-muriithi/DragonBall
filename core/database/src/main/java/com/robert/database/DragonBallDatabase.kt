package com.robert.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robert.database.dao.CharacterDao
import com.robert.database.dao.CharacterDetailsDao
import com.robert.database.dao.CharacterRemoteKeysDao
import com.robert.database.dao.PlanetDao
import com.robert.database.dao.TransformationDao
import com.robert.database.entity.CharacterDetailsEntity
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.CharacterRemoteKeysEntity
import com.robert.database.entity.PlanetEntity
import com.robert.database.entity.TransformationEntity

@Database(
    entities = [
        CharacterEntity::class,
        CharacterRemoteKeysEntity::class,
        CharacterDetailsEntity::class,
        PlanetEntity::class,
        TransformationEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class DragonBallDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterRemoteKeysDao(): CharacterRemoteKeysDao
    abstract fun characterDetailsDao(): CharacterDetailsDao
    abstract fun planetDao(): PlanetDao
    abstract fun transformationDao(): TransformationDao
}

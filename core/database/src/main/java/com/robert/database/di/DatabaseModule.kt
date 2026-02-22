package com.robert.database.di

import android.content.Context
import androidx.room.Room
import com.robert.database.DatabaseConstants.DATABASE_NAME
import com.robert.database.DragonBallDatabase
import com.robert.database.dao.CharacterDao
import com.robert.database.dao.CharacterDetailsDao
import com.robert.database.dao.CharacterRemoteKeysDao
import com.robert.database.dao.PlanetDao
import com.robert.database.dao.TransformationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDragonBallDatabase(
        @ApplicationContext context: Context
    ): DragonBallDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DragonBallDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: DragonBallDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    @Singleton
    fun provideCharacterRemoteKeysDao(database: DragonBallDatabase): CharacterRemoteKeysDao {
        return database.characterRemoteKeysDao()
    }

    @Provides
    @Singleton
    fun provideCharacterDetailsDao(database: DragonBallDatabase): CharacterDetailsDao {
        return database.characterDetailsDao()
    }

    @Provides
    @Singleton
    fun providePlanetDao(database: DragonBallDatabase): PlanetDao {
        return database.planetDao()
    }

    @Provides
    @Singleton
    fun provideTransformationDao(database: DragonBallDatabase): TransformationDao {
        return database.transformationDao()
    }
}


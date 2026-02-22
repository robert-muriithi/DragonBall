package com.robert.data.di

import com.robert.data.repository.CharacterRepositoryImpl
import com.robert.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCharacterRepository(
        impl: CharacterRepositoryImpl
    ): CharacterRepository
}


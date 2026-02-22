package com.robert.dragonball

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.robert.datastore.ThemeDataStore
import com.robert.domain.repository.CharacterRepository
import com.robert.domain.usecase.GetCharacterDetailsUseCase
import com.robert.domain.usecase.GetCharactersUseCase
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HiltDependencyTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var characterRepository: CharacterRepository

    @Inject
    lateinit var getCharactersUseCase: GetCharactersUseCase

    @Inject
    lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase

    @Inject
    lateinit var themeDataStore: ThemeDataStore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun characterRepository_isInjected() {
        assertThat(characterRepository).isNotNull()
    }

    @Test
    fun getCharactersUseCase_isInjected() {
        assertThat(getCharactersUseCase).isNotNull()
    }

    @Test
    fun getCharacterDetailsUseCase_isInjected() {
        assertThat(getCharacterDetailsUseCase).isNotNull()
    }

    @Test
    fun themeDataStore_isInjected() {
        assertThat(themeDataStore).isNotNull()
    }
}

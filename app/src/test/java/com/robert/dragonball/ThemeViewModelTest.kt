package com.robert.dragonball

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.robert.datastore.ThemeDataStore
import com.robert.datastore.ThemePreference
import com.robert.dragonball.theme.ThemeViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val themeDataStore = mockk<ThemeDataStore>(relaxed = true)
    private lateinit var viewModel: ThemeViewModel

    @Before
    fun setup() {
        every { themeDataStore.themePreference } returns flowOf(ThemePreference.SYSTEM)
        viewModel = ThemeViewModel(themeDataStore)
    }


    @Test
    fun `initial theme preference is SYSTEM`() {
        assertThat(viewModel.themePreference.value).isEqualTo(ThemePreference.SYSTEM)
    }


    @Test
    fun `toggleTheme calls setTheme with DARK when current is LIGHT`() = runTest {
        every { themeDataStore.themePreference } returns flowOf(ThemePreference.LIGHT)
        viewModel = ThemeViewModel(themeDataStore)

        viewModel.toggleTheme()

        coVerify { themeDataStore.setTheme(ThemePreference.DARK) }
    }

    @Test
    fun `toggleTheme calls setTheme with SYSTEM when current is DARK`() = runTest {
        every { themeDataStore.themePreference } returns flowOf(ThemePreference.DARK)
        viewModel = ThemeViewModel(themeDataStore)

        viewModel.toggleTheme()

        coVerify { themeDataStore.setTheme(ThemePreference.SYSTEM) }
    }

    @Test
    fun `toggleTheme calls setTheme with LIGHT when current is SYSTEM`() = runTest {
        every { themeDataStore.themePreference } returns flowOf(ThemePreference.SYSTEM)
        viewModel = ThemeViewModel(themeDataStore)

        viewModel.toggleTheme()

        coVerify { themeDataStore.setTheme(ThemePreference.LIGHT) }
    }


    @Test
    fun `themePreference StateFlow reflects upstream changes`() = runTest {
        val preferences = MutableStateFlow(ThemePreference.SYSTEM)
        every { themeDataStore.themePreference } returns preferences
        viewModel = ThemeViewModel(themeDataStore)

        viewModel.themePreference.test {
            assertThat(awaitItem()).isEqualTo(ThemePreference.SYSTEM)

            preferences.value = ThemePreference.DARK
            assertThat(awaitItem()).isEqualTo(ThemePreference.DARK)

            preferences.value = ThemePreference.LIGHT
            assertThat(awaitItem()).isEqualTo(ThemePreference.LIGHT)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

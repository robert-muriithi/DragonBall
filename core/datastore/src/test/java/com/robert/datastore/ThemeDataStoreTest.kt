package com.robert.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeDataStoreTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var themeDataStore: ThemeDataStore

    @Before
    fun setup() {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tmpFolder.newFile("test_theme.preferences_pb") }
        )
        themeDataStore = ThemeDataStore(dataStore)
    }

    @Test
    fun `initial theme preference is SYSTEM when nothing is stored`() = testScope.runTest {
        themeDataStore.themePreference.test {
            assertThat(awaitItem()).isEqualTo(ThemePreference.SYSTEM)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setTheme LIGHT emits LIGHT`() = testScope.runTest {
        themeDataStore.setTheme(ThemePreference.LIGHT)

        themeDataStore.themePreference.test {
            assertThat(awaitItem()).isEqualTo(ThemePreference.LIGHT)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setTheme DARK emits DARK`() = testScope.runTest {
        themeDataStore.setTheme(ThemePreference.DARK)

        themeDataStore.themePreference.test {
            assertThat(awaitItem()).isEqualTo(ThemePreference.DARK)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setTheme SYSTEM emits SYSTEM`() = testScope.runTest {
        themeDataStore.setTheme(ThemePreference.LIGHT)
        themeDataStore.setTheme(ThemePreference.SYSTEM)

        themeDataStore.themePreference.test {
            assertThat(awaitItem()).isEqualTo(ThemePreference.SYSTEM)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `successive setTheme calls emit each value in order`() = testScope.runTest {
        themeDataStore.themePreference.test {
            assertThat(awaitItem()).isEqualTo(ThemePreference.SYSTEM)

            themeDataStore.setTheme(ThemePreference.LIGHT)
            assertThat(awaitItem()).isEqualTo(ThemePreference.LIGHT)

            themeDataStore.setTheme(ThemePreference.DARK)
            assertThat(awaitItem()).isEqualTo(ThemePreference.DARK)

            themeDataStore.setTheme(ThemePreference.SYSTEM)
            assertThat(awaitItem()).isEqualTo(ThemePreference.SYSTEM)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

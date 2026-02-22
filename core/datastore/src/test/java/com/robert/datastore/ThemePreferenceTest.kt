package com.robert.datastore

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ThemePreferenceTest {

    @Test
    fun `LIGHT next returns DARK`() {
        assertThat(ThemePreference.LIGHT.getTheme()).isEqualTo(ThemePreference.DARK)
    }

    @Test
    fun `DARK next returns SYSTEM`() {
        assertThat(ThemePreference.DARK.getTheme()).isEqualTo(ThemePreference.SYSTEM)
    }

    @Test
    fun `SYSTEM next returns LIGHT`() {
        assertThat(ThemePreference.SYSTEM.getTheme()).isEqualTo(ThemePreference.LIGHT)
    }

    @Test
    fun `cycling through all values returns to start`() {
        val start = ThemePreference.LIGHT
        val cycled = start.getTheme().getTheme().getTheme()
        assertThat(cycled).isEqualTo(start)
    }

    @Test
    fun `all enum entries are distinct`() {
        val values = ThemePreference.entries
        assertThat(values.toSet()).hasSize(values.size)
    }
}

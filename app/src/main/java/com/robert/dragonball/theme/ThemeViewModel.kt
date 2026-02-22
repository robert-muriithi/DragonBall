package com.robert.dragonball.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.datastore.ThemeDataStore
import com.robert.datastore.ThemePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    val themePreference: StateFlow<ThemePreference> = themeDataStore.themePreference
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemePreference.SYSTEM
        )

    fun toggleTheme() {
        viewModelScope.launch {
            themeDataStore.setTheme(themePreference.value.getTheme())
        }
    }
}

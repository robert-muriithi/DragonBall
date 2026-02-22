package com.robert.dragonball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robert.common.dispatcher.DispatcherProvider
import com.robert.common.dispatcher.LocalDispatcherProvider
import com.robert.datastore.ThemePreference
import com.robert.designsystem.theme.DragonBallTheme
import com.robert.dragonball.navigation.DragonBallNavHost
import com.robert.dragonball.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var dispatchers: DispatcherProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themePreference by themeViewModel.themePreference.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()

            val isDark = when (themePreference) {
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
                ThemePreference.SYSTEM -> systemDark
            }

            CompositionLocalProvider(LocalDispatcherProvider provides dispatchers) {
                DragonBallTheme(darkTheme = isDark) {
                    DragonBallNavHost(
                        modifier = Modifier.fillMaxSize(),
                        isDarkTheme = isDark,
                        onThemeToggle = themeViewModel::toggleTheme
                    )
                }
            }
        }
    }
}



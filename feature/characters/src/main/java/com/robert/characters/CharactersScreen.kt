package com.robert.characters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.robert.characters.components.AffiliationFilterChips
import com.robert.characters.components.CharacterList
import com.robert.characters.components.SearchBar
import com.robert.designsystem.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.CharactersScreen(
    viewModel: CharactersViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val characters = viewModel.characters.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CharactersAppBar(
                onThemeToggle = onThemeToggle,
                isDarkTheme = isDarkTheme,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Spacing.default),
            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onQueryChange
            )

            AnimatedVisibility(
                visible = !uiState.isSearchActive,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                AffiliationFilterChips(
                    selectedAffiliation = uiState.selectedAffiliation,
                    onAffiliationSelected = viewModel::onAffiliationChange
                )
            }

            CharacterList(
                characters = characters,
                animatedVisibilityScope = animatedVisibilityScope,
                onCharacterClick = onCharacterClick,
                selectedAffiliation = uiState.selectedAffiliation,
                searchQuery = uiState.searchQuery
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CharactersAppBar(
    onThemeToggle: () -> Unit,
    isDarkTheme: Boolean,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(stringResource(R.string.dragon_ball)) },
        actions = {
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (isDarkTheme) stringResource(R.string.switch_to_light_mode) else stringResource(
                        R.string.switch_to_dark_mode
                    )
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}


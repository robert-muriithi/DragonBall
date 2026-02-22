package com.robert.characters.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.robert.characters.model.CharacterSummaryModel
import com.robert.designsystem.theme.Spacing

@Composable
fun SharedTransitionScope.CharacterList(
    characters: LazyPagingItems<CharacterSummaryModel>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onCharacterClick: (Int) -> Unit,
    selectedAffiliation: String?,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val isEmptyAfterLoad = characters.itemCount == 0 &&
            characters.loadState.refresh is LoadState.NotLoading

    if (isEmptyAfterLoad) {
        EmptyStateContent(
            selectedAffiliation = selectedAffiliation,
            searchQuery = searchQuery,
            modifier = modifier
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.default),
            horizontalArrangement = Arrangement.spacedBy(Spacing.default),
            contentPadding = PaddingValues(bottom = Spacing.default)
        ) {
            items(
                count = characters.itemCount,
                key = characters.itemKey { it.id }
            ) { index ->
                characters[index]?.let { character ->
                    CharacterCard(
                        item = character,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onClick = { onCharacterClick(character.id) }
                    )
                }
            }

            if (characters.loadState.refresh is LoadState.Loading) {
                item {
                    LoadingIndicator()
                }
            }

            if (characters.loadState.append is LoadState.Loading) {
                item {
                    LoadingIndicator()
                }
            }
        }
    }
}


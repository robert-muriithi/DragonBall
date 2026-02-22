package com.robert.character_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robert.character_details.components.BiographySection
import com.robert.character_details.components.HeroImageSection
import com.robert.character_details.components.InfoChip
import com.robert.character_details.components.PlanetCard
import com.robert.character_details.components.SectionHeader
import com.robert.character_details.components.StatItem
import com.robert.character_details.components.TransformationCard
import com.robert.character_details.model.CharacterDetailsModel
import com.robert.character_details.model.PlanetModel
import com.robert.common.extensions.orDash
import com.robert.common.extensions.orUnknown
import com.robert.designsystem.theme.DragonBallTheme
import com.robert.designsystem.theme.Elevation
import com.robert.designsystem.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.CharacterDetailsScreen(
    viewModel: CharacterDetailsViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CharacterDetailsAppBar(
                uiState = uiState,
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingContent(modifier = Modifier.padding(innerPadding))
            uiState.isError -> ErrorContent(
                message = uiState.errorMessage ?: stringResource(R.string.unknown_error),
                onRetry = { viewModel.retry() },
                modifier = Modifier.padding(innerPadding)
            )
            uiState.isSuccess -> uiState.character?.let { details ->
                CharacterDetailsContent(
                    details = details,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            uiState.isEmpty -> EmptyContent(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CharacterDetailsAppBar(
    uiState: CharacterDetailsUiState,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(
                text = uiState.character?.name ?: stringResource(R.string.character_details)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.large),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.default)
        ) {
            Text(message)
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.large),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.no_character_details_available))
    }
}

@Composable
private fun SharedTransitionScope.CharacterDetailsContent(
    details: CharacterDetailsModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            HeroImageSection(
                imageUrl = details.image,
                characterId = details.id,
                characterName = details.name,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }

        item {
            CharacterInfoSection(
                details = details,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }

        details.description?.let { description ->
            item {
                BiographySection(description = description)
            }
        }

        item {
            PlanetSection(planet = details.originPlanet)
        }

        item {
            TransformationsHeader(isEmpty = details.transformations.isEmpty())
        }

        if (details.transformations.isNotEmpty()) {
            items(details.transformations.size) { index ->
                TransformationCard(
                    transformation = details.transformations[index],
                    modifier = Modifier.padding(
                        horizontal = Spacing.default,
                        vertical = Spacing.extraSmall
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(Spacing.large))
        }
    }
}

@Composable
private fun SharedTransitionScope.CharacterInfoSection(
    details: CharacterDetailsModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.default),
        verticalArrangement = Arrangement.spacedBy(Spacing.default)
    ) {

        Text(
            text = details.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            modifier = Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "name_${details.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.default)
        ) {
            InfoChip(label = stringResource(R.string.race), value = details.race.orUnknown())
            InfoChip(label = stringResource(R.string.status), value = details.status.orUnknown())
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = Elevation.small)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.default),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = stringResource(R.string.base_ki), value = details.ki.orDash())
                StatItem(label = stringResource(R.string.max_ki), value = details.maxKi.orDash())
            }
        }

        SectionHeader(title = stringResource(R.string.affiliation))
        Text(
            text = details.affiliation.orUnknown(),
            style = MaterialTheme.typography.bodyLarge,
            color = colors.affiliationColor
        )
    }
}

@Composable
private fun PlanetSection(
    planet: PlanetModel?,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.default)
            .padding(top = Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        SectionHeader(title = stringResource(R.string.origin_planet))

        if (planet == null) {
            Text(
                text = stringResource(R.string.unknown),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        } else {
            PlanetCard(planet = planet)
        }
    }
}

@Composable
private fun TransformationsHeader(
    isEmpty: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.default)
            .padding(top = Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        SectionHeader(title = stringResource(R.string.transformations))

        if (isEmpty) {
            Text(
                text = stringResource(R.string.no_transformations_available),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        }
    }
}


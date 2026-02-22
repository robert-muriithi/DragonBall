package com.robert.characters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.robert.characters.R
import com.robert.designsystem.theme.DragonBallOrange
import com.robert.designsystem.theme.Spacing
import com.robert.designsystem.theme.DragonBallTheme

@Composable
fun EmptyStateContent(
    selectedAffiliation: String?,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    val title: String
    val subtitle: String

    when {
        selectedAffiliation != null -> {
            title = stringResource(R.string.no_characters, selectedAffiliation)
            subtitle = stringResource(R.string.no_character_description)
        }
        searchQuery.isNotBlank() -> {
            title = stringResource(R.string.no_results_for, searchQuery)
            subtitle = stringResource(R.string.no_results_description)
        }
        else -> {
            title = stringResource(R.string.no_characters_found)
            subtitle = stringResource(R.string.try_again_later)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(DragonBallOrange.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = DragonBallOrange
            )
        }

        Spacer(modifier = Modifier.height(Spacing.large))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

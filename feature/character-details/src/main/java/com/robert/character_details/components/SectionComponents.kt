package com.robert.character_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.robert.character_details.R
import com.robert.designsystem.theme.DragonBallTheme
import com.robert.designsystem.theme.Spacing

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = colors.textPrimary,
        modifier = modifier
    )
}

@Composable
fun BiographySection(
    description: String,
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
        SectionHeader(title = stringResource(R.string.biography))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary
        )
    }
}


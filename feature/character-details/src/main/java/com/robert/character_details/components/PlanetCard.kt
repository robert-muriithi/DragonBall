package com.robert.character_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.robert.character_details.R
import com.robert.character_details.model.PlanetModel
import com.robert.designsystem.theme.DragonBallTheme
import com.robert.designsystem.theme.Elevation
import com.robert.designsystem.theme.Spacing
import com.robert.domain.model.Planet

@Composable
fun PlanetCard(
    planet: PlanetModel,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.small)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            planet.image?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = planet.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = planet.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                Text(
                    text = if (planet.isDestroyed == true) stringResource(R.string.destroyed) else stringResource(
                        R.string.intact
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (planet.isDestroyed == true) colors.statusDeceased else colors.statusAlive
                )
            }
        }
    }
}


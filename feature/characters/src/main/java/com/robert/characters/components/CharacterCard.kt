package com.robert.characters.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.robert.characters.R
import com.robert.characters.model.CharacterSummaryModel
import com.robert.common.extensions.orDash
import com.robert.common.extensions.orUnknown
import com.robert.designsystem.theme.AspectRatio
import com.robert.designsystem.theme.DragonBallTheme
import com.robert.designsystem.theme.Elevation
import com.robert.designsystem.theme.Spacing
import com.robert.characters.util.rememberDominantColor

@Composable
fun SharedTransitionScope.CharacterCard(
    item: CharacterSummaryModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors
    val dominantColor = rememberDominantColor(
        imageUrl = item.image,
        defaultColor = colors.cardBackground
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.small)
    ) {
        Column {
            val cardBackground = colors.cardBackground
            val tintColor = dominantColor.copy(alpha = 0.5f)
            val gradientColors = listOf(
                Color.Transparent,
                dominantColor.copy(alpha = 0.4f),
                cardBackground
            )

            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(AspectRatio.characterCard)
                    .background(tintColor)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "image_${item.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = gradientColors,
                                startY = size.height * 0.55f,
                                endY = size.height
                            )
                        )
                    },
                contentScale = ContentScale.Fit
            )

            CharacterCardContent(item = item)
        }
    }
}

@Composable
private fun CharacterCardContent(
    item: CharacterSummaryModel,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${item.race.orUnknown()} • ${item.gender.orUnknown()}",
            style = MaterialTheme.typography.labelSmall,
            color = colors.accentOrange,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.base_ki),
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary,
            )
            Text(
                text = item.ki.orDash(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.kiColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.total_ki),
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary
            )
            Text(
                text = item.maxKi.orDash(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.kiColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.affiliation),
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary
            )
            Text(
                text = item.affiliation.orUnknown(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.affiliationColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CharacterCardPreviewContent(
    item: CharacterSummaryModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = DragonBallTheme.colors

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.default)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(AspectRatio.characterCard)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    colors.cardBackground
                                )
                            )
                        )
                )
            }

            CharacterCardContent(item = item)
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun CharacterCardPreview() {
    DragonBallTheme(darkTheme = false) {
        CharacterCardPreviewContent(
            item = CharacterSummaryModel(
                id = 1,
                name = "Piccolo",
                race = "Namekian",
                gender = "Male",
                affiliation = "Z Fighter",
                image = "",
                ki = "2.000.000",
                maxKi = "500.000.000"
            ),
            modifier = Modifier.padding(Spacing.default)
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun CharacterCardGokuPreview() {
    DragonBallTheme(darkTheme = true) {
        CharacterCardPreviewContent(
            item = CharacterSummaryModel(
                id = 2,
                name = "Goku",
                race = "Saiyan",
                gender = "Male",
                affiliation = "Z Fighter",
                image = "",
                ki = "60.000.000",
                maxKi = "90 Septillion"
            ),
            modifier = Modifier.padding(Spacing.default)
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun CharacterCardVegetaPreview() {
    DragonBallTheme(darkTheme = true) {
        CharacterCardPreviewContent(
            item = CharacterSummaryModel(
                id = 3,
                name = "Vegeta",
                race = "Saiyan",
                gender = "Male",
                affiliation = "Z Fighter",
                image = "",
                ki = "54.000.000",
                maxKi = "19.84 Septillion"
            ),
            modifier = Modifier.padding(Spacing.default)
        )
    }
}


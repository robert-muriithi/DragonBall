package com.robert.character_details.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.robert.designsystem.theme.AspectRatio

@Composable
fun SharedTransitionScope.HeroImageSection(
    imageUrl: String?,
    characterId: Int,
    characterName: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val gradientColors = listOf(Color.Transparent, backgroundColor)

    AsyncImage(
        model = imageUrl,
        contentDescription = characterName,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(AspectRatio.characterDetail)
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = "image_$characterId"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = size.height * 0.5f,
                        endY = size.height
                    )
                )
            },
        contentScale = ContentScale.Fit
    )
}


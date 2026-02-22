package com.robert.character_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.robert.designsystem.theme.DragonBallTheme

@Composable
fun InfoChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val colors = DragonBallTheme.colors

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.accentOrange
        )
    }
}


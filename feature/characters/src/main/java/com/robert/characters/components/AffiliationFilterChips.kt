package com.robert.characters.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.robert.characters.R
import com.robert.common.constants.Affiliations
import com.robert.designsystem.theme.Spacing

@Composable
fun AffiliationFilterChips(
    selectedAffiliation: String?,
    onAffiliationSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        FilterChip(
            text = stringResource(R.string.all),
            isSelected = selectedAffiliation == null,
            onClick = { onAffiliationSelected(null) }
        )

        Affiliations.ALL.forEach { affiliation ->
            FilterChip(
                text = affiliation,
                isSelected = selectedAffiliation == affiliation,
                onClick = { onAffiliationSelected(affiliation) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AssistChip(
        onClick = onClick,
        label = { Text(text) },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    )
}


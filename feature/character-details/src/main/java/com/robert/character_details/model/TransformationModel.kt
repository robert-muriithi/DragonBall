package com.robert.character_details.model

import androidx.compose.runtime.Immutable
import com.robert.domain.model.Transformation

@Immutable
data class TransformationModel(
    val id: Int,
    val name: String,
    val image: String?,
    val ki: String?
)


package com.robert.character_details

import androidx.compose.runtime.Immutable
import com.robert.character_details.model.CharacterDetailsModel

@Immutable
data class CharacterDetailsUiState(
    val isLoading: Boolean = false,
    val character: CharacterDetailsModel? = null,
    val errorMessage: String? = null
) {
    val isError: Boolean get() = errorMessage != null
    val isSuccess: Boolean get() = character != null && !isLoading && !isError
    val isEmpty: Boolean get() = character == null && !isLoading && !isError
}


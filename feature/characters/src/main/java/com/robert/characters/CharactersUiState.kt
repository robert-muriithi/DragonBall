package com.robert.characters

data class CharactersUiState(
    val searchQuery: String = "",
    val selectedAffiliation: String? = null
) {
    val isSearchActive: Boolean get() = searchQuery.isNotBlank()
}
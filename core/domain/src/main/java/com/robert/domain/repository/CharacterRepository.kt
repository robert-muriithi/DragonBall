package com.robert.domain.repository

import androidx.paging.PagingData
import com.robert.domain.model.CharacterDetails
import com.robert.domain.model.CharacterSummary
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersPaged(
        affiliation: String?,
        searchQuery: String?
    ): Flow<PagingData<CharacterSummary>>

    suspend fun getCharacterDetails(characterId: Int): CharacterDetails
}

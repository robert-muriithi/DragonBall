package com.robert.domain.usecase

import androidx.paging.PagingData
import com.robert.domain.model.CharacterSummary
import com.robert.domain.model.Params
import com.robert.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(params: Params): Flow<PagingData<CharacterSummary>> {
        return repository.fetchPagedCharacters(
            affiliation = params.affiliation?.takeIf { it.isNotBlank() },
            searchQuery = params.searchQuery?.takeIf { it.isNotBlank() }
        )
    }
}



package com.robert.domain.usecase

import com.robert.domain.model.CharacterDetails
import com.robert.domain.repository.CharacterRepository
import javax.inject.Inject


class GetCharacterDetailsUseCase @Inject constructor(
    private val repository: CharacterRepository
)  {
    suspend operator fun invoke(params: Int): Result<CharacterDetails> {
        return runCatching {
            repository.getCharacterDetails(params)
        }
    }
}


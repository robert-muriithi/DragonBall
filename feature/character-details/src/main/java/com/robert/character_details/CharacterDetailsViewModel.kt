package com.robert.character_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.character_details.mapper.toCharacterDetailsUi
import com.robert.common.error.ErrorHandler
import com.robert.domain.usecase.GetCharacterDetailsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CharacterDetailsViewModel.Factory::class)
class CharacterDetailsViewModel @AssistedInject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    @Assisted("characterId") private val characterId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()


    init {
        fetchCharacterDetails(characterId = characterId)
    }

    fun fetchCharacterDetails(characterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getCharacterDetailsUseCase(characterId)
                .onSuccess { details ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            character = details.toCharacterDetailsUi(),
                            errorMessage = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = ErrorHandler.getErrorMessage(throwable)
                        )
                    }
                }
        }
    }

    fun retry() {
        fetchCharacterDetails(characterId)
    }

    @AssistedFactory interface Factory {
        fun create(
           @Assisted("characterId") characterId: Int
        ): CharacterDetailsViewModel
    }
}


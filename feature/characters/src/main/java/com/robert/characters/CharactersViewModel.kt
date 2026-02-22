package com.robert.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.robert.characters.mapper.toCharacterSummaryUin
import com.robert.characters.model.CharacterSummaryModel
import com.robert.domain.model.CharacterSummary
import com.robert.domain.model.Params
import com.robert.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    val characters: Flow<PagingData<CharacterSummaryModel>> = _uiState
        .debounce(DEBOUNCE_TIMEOUT_MS)
        .distinctUntilChanged { old, new ->
            old.searchQuery == new.searchQuery && old.selectedAffiliation == new.selectedAffiliation
        }
        .flatMapLatest { state ->
            Timber.d("Fetching characters - query: '${state.searchQuery}', affiliation: ${state.selectedAffiliation}")
            getCharactersUseCase(
                Params(
                    searchQuery = state.searchQuery,
                    affiliation = state.selectedAffiliation
                )
            )
        }
        .map {
            it.map { characterSummary ->
                characterSummary.toCharacterSummaryUin()
            }
        }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                selectedAffiliation = if (query.isBlank()) null else it.selectedAffiliation
            )
        }
    }

    fun onAffiliationChange(affiliation: String?) {
        _uiState.update { it.copy(selectedAffiliation = affiliation) }
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT_MS = 300L
    }
}


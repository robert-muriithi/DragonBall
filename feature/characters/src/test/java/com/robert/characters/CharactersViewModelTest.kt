package com.robert.characters

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.robert.domain.model.CharacterSummary
import com.robert.domain.model.Params
import com.robert.domain.usecase.GetCharactersUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCharactersUseCase = mockk<GetCharactersUseCase>()
    private lateinit var viewModel: CharactersViewModel

    @Before
    fun setup() {
        every { getCharactersUseCase(any()) } returns flowOf(PagingData.empty())
        viewModel = CharactersViewModel(getCharactersUseCase)
    }


    @Test
    fun `initial state has empty search query`() {
        assertThat(viewModel.uiState.value.searchQuery).isEmpty()
    }

    @Test
    fun `initial state has null affiliation`() {
        assertThat(viewModel.uiState.value.selectedAffiliation).isNull()
    }

    @Test
    fun `initial isSearchActive is false`() {
        assertThat(viewModel.uiState.value.isSearchActive).isFalse()
    }


    @Test
    fun `onQueryChange updates searchQuery in state`() = runTest {
        viewModel.onQueryChange("Goku")
        assertThat(viewModel.uiState.value.searchQuery).isEqualTo("Goku")
    }

    @Test
    fun `onQueryChange with non-blank query sets isSearchActive to true`() = runTest {
        viewModel.onQueryChange("Vegeta")
        assertThat(viewModel.uiState.value.isSearchActive).isTrue()
    }

    @Test
    fun `onQueryChange with blank query sets isSearchActive to false`() = runTest {
        viewModel.onQueryChange("Goku")
        viewModel.onQueryChange("")
        assertThat(viewModel.uiState.value.isSearchActive).isFalse()
    }

    @Test
    fun `onQueryChange with blank string clears selected affiliation`() = runTest {
        viewModel.onAffiliationChange("Z Fighter")
        viewModel.onQueryChange("")
        assertThat(viewModel.uiState.value.selectedAffiliation).isNull()
    }

    @Test
    fun `onQueryChange with non-blank string preserves selected affiliation`() = runTest {
        viewModel.onAffiliationChange("Z Fighter")
        viewModel.onQueryChange("Goku")
        assertThat(viewModel.uiState.value.selectedAffiliation).isEqualTo("Z Fighter")
    }


    @Test
    fun `onAffiliationChange updates selectedAffiliation`() = runTest {
        viewModel.onAffiliationChange("Villain")
        assertThat(viewModel.uiState.value.selectedAffiliation).isEqualTo("Villain")
    }

    @Test
    fun `onAffiliationChange with null clears affiliation`() = runTest {
        viewModel.onAffiliationChange("Z Fighter")
        viewModel.onAffiliationChange(null)
        assertThat(viewModel.uiState.value.selectedAffiliation).isNull()
    }

    @Test
    fun `onAffiliationChange does not reset search query`() = runTest {
        viewModel.onQueryChange("Goku")
        viewModel.onAffiliationChange("Z Fighter")
        assertThat(viewModel.uiState.value.searchQuery).isEqualTo("Goku")
    }


    @Test
    fun `uiState emits updated state on queryChange`() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem().searchQuery).isEmpty()

            viewModel.onQueryChange("Frieza")
            assertThat(awaitItem().searchQuery).isEqualTo("Frieza")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits updated state on affiliationChange`() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem().selectedAffiliation).isNull()

            viewModel.onAffiliationChange("Namekian")
            assertThat(awaitItem().selectedAffiliation).isEqualTo("Namekian")

            cancelAndIgnoreRemainingEvents()
        }
    }
}

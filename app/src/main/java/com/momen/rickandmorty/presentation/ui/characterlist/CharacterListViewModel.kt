package com.momen.rickandmorty.presentation.ui.characterlist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momen.rickandmorty.domain.model.Character
import com.momen.rickandmorty.domain.usecase.SearchCharactersUseCase
import com.momen.rickandmorty.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterListState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String = "",
    val loadMoreError: String = "",
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val isLoadingMore: Boolean = false,
    val hasNextPage: Boolean = false
)

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val searchCharactersUseCase: SearchCharactersUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CharacterListState())
    val state: State<CharacterListState> = _state

    private var searchJob: Job? = null

    init {
        getCharacters()
    }

    fun onEvent(event: CharacterListEvent) {
        when (event) {
            is CharacterListEvent.Search -> {
                searchCharacters(event.query)
            }

            is CharacterListEvent.LoadMore -> {
                loadMoreCharacters()
            }

            is CharacterListEvent.Retry -> {
                retryCharacters()
            }
        }
    }

    private fun getCharacters(page: Int = 1, query: String = "") {
        viewModelScope.launch {
            searchCharactersUseCase(query, page).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            characters = if (page == 1) result.data?.characters ?: emptyList()
                            else _state.value.characters + (result.data?.characters ?: emptyList()),
                            isLoading = false,
                            isLoadingMore = false,
                            error = "",
                            hasNextPage = result.data?.hasNextPage ?: false
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = result.message ?: "An unexpected error occurred",

                            characters = if (page == 1 && query.isNotBlank()) emptyList() else _state.value.characters,
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = page == 1,
                            isLoadingMore = page > 1
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private fun searchCharacters(query: String) {
        Log.d("CharacterListViewModel", "Search query: $query")
        searchJob?.cancel()

        _state.value = _state.value.copy(
            searchQuery = query,
            currentPage = 1,
        )

        searchJob = viewModelScope.launch {
            delay(500L)
            _state.value = _state.value.copy(
                characters = emptyList(),
                hasNextPage = true
            )

            getCharacters(query = query)
        }
    }

    private fun loadMoreCharacters() {
        if (_state.value.isLoadingMore || !_state.value.hasNextPage || _state.value.error.isNotBlank()) return

        val nextPage = _state.value.currentPage + 1
        _state.value = _state.value.copy(currentPage = nextPage)
        getCharacters(nextPage, _state.value.searchQuery)
    }

    private fun retryCharacters() {
        _state.value = _state.value.copy(
            error = ""
        )
        getCharacters(page = _state.value.currentPage, query = _state.value.searchQuery)
    }

}

sealed class CharacterListEvent {
    data class Search(val query: String) : CharacterListEvent()
    object LoadMore : CharacterListEvent()
    object Retry : CharacterListEvent()
}
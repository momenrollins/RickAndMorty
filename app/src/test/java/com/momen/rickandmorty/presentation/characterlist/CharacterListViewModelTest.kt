package com.momen.rickandmorty.presentation.characterlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.momen.rickandmorty.domain.model.CharacterListResult
import com.momen.rickandmorty.domain.usecase.SearchCharactersUseCase
import com.momen.rickandmorty.domain.util.Resource
import com.momen.rickandmorty.presentation.ui.characterlist.CharacterListEvent
import com.momen.rickandmorty.presentation.ui.characterlist.CharacterListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CharacterListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var searchCharactersUseCase: SearchCharactersUseCase

    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Mock initial load
        runBlocking {
            whenever(searchCharactersUseCase.invoke(any(), any())).thenReturn(
                flowOf(
                    Resource.Success(
                        CharacterListResult(
                            characters = emptyList(),
                            hasNextPage = false
                        )
                    )
                )
            )
        }

        viewModel = CharacterListViewModel(searchCharactersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search event updates search query`() = runTest {
        // Given
        val query = "Rick"
        whenever(searchCharactersUseCase(query, 1)).thenReturn(
            flowOf(
                Resource.Success(
                    CharacterListResult(
                        characters = emptyList(),
                        hasNextPage = false
                    )
                )
            )
        )

        // When
        viewModel.onEvent(CharacterListEvent.Search(query))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(query, viewModel.state.value.searchQuery)

    }
}
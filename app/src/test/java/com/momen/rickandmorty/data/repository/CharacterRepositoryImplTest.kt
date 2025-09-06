package com.momen.rickandmorty.data.repository

import com.momen.rickandmorty.data.api.RickMortyApi
import com.momen.rickandmorty.data.api.dto.CharacterDto
import com.momen.rickandmorty.data.api.dto.CharacterResponseDto
import com.momen.rickandmorty.data.api.dto.InfoDto
import com.momen.rickandmorty.data.api.dto.LocationDto
import com.momen.rickandmorty.data.api.dto.OriginDto
import com.momen.rickandmorty.domain.util.Resource
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class CharacterRepositoryImplTest {

    @Mock
    private lateinit var api: RickMortyApi

    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = CharacterRepositoryImpl(api)
    }

    @Test
    fun `searchCharacters returns success when api call is successful`() = runTest {
        // Given
        val mockResponse = CharacterResponseDto(
            info = InfoDto(1, 1, null, null),
            results = listOf(
                CharacterDto(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    origin = OriginDto("Earth", ""),
                    location = LocationDto("Earth", ""),
                    image = "image.jpg",
                    episode = emptyList(),
                    url = "",
                    created = ""
                )
            )
        )
        whenever(api.searchCharacters(name = "Rick", page = 1)).thenReturn(mockResponse)

        // When
        val result = repository.searchCharacters(name = "Rick", page = 1).last()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, result.data?.characters?.size)
        assertEquals("Rick Sanchez", result.data?.characters?.get(0)?.name)

    }
}
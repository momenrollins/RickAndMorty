package com.momen.rickandmorty.domain.usecase

import com.momen.rickandmorty.data.repository.CharacterRepository
import com.momen.rickandmorty.domain.model.Character
import com.momen.rickandmorty.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(name: String, page: Int): Flow<Resource<List<Character>>> {
        return repository.searchCharacters(name, page)
    }
}
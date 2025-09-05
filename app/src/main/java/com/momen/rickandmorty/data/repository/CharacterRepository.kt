package com.momen.rickandmorty.data.repository

import com.momen.rickandmorty.domain.util.Resource
import com.momen.rickandmorty.domain.model.Character
import com.momen.rickandmorty.domain.model.CharacterListResult

import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(page: Int): Flow<Resource<List<Character>>>
    suspend fun searchCharacters(name: String, page: Int): Flow<Resource<CharacterListResult>>
}
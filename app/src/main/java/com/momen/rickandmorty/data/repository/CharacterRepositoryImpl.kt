package com.momen.rickandmorty.data.repository

import com.momen.rickandmorty.data.api.RickMortyApi
import com.momen.rickandmorty.data.mapper.toCharacter
import com.momen.rickandmorty.domain.model.Character
import com.momen.rickandmorty.domain.model.CharacterListResult
import com.momen.rickandmorty.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val api: RickMortyApi
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Flow<Resource<List<Character>>> = flow {
        try {
            emit(Resource.Loading())
            val characters = api.getCharacters(page).results.map { it.toCharacter() }
            emit(Resource.Success(characters))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun searchCharacters(
        name: String,
        page: Int
    ): Flow<Resource<CharacterListResult>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.searchCharacters(name, page)
            val characters = response.results.map { it.toCharacter() }
            val characterListResult = CharacterListResult(
                characters = characters,
                hasNextPage = response.info.next != null,
                currentPage = page
            )
            emit(Resource.Success(characterListResult))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: HttpException) {
            val message = when (e.code()) {
                404 -> if (name.isNotBlank()) "No characters found for '$name'" else "No characters found"
                else -> "Something went wrong. Please try again."
            }
            emit(Resource.Error(message))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }
}
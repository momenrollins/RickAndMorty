package com.momen.rickandmorty.data.api

import com.momen.rickandmorty.data.api.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickMortyApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): CharacterResponseDto

    @GET("character")
    suspend fun searchCharacters(
        @Query("name") name: String,
        @Query("page") page: Int = 1
    ): CharacterResponseDto
}
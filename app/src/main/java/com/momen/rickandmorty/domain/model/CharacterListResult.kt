
package com.momen.rickandmorty.domain.model

data class CharacterListResult(
    val characters: List<Character>,
    val hasNextPage: Boolean,
    val currentPage: Int
)
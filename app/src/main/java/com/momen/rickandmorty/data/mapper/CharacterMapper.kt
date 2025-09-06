package com.momen.rickandmorty.data.mapper

import com.momen.rickandmorty.data.api.dto.CharacterDto
import com.momen.rickandmorty.data.api.dto.CharacterResponseDto
import com.momen.rickandmorty.domain.model.Character
import com.momen.rickandmorty.domain.model.CharacterListResult

fun CharacterDto.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        image = image
    )
}

fun CharacterResponseDto.toCharacterListResult(): CharacterListResult {
    return CharacterListResult(
        characters = results.map { it.toCharacter() },
        hasNextPage = info.next != null
    )
}
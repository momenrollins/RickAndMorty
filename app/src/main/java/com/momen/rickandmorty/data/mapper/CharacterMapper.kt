package com.momen.rickandmorty.data.mapper

import com.momen.rickandmorty.data.api.dto.CharacterDto
import com.momen.rickandmorty.domain.model.Character

fun CharacterDto.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        image = image
    )
}
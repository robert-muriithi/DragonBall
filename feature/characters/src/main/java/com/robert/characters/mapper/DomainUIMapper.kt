package com.robert.characters.mapper

import com.robert.characters.model.CharacterSummaryModel
import com.robert.domain.model.CharacterSummary

fun CharacterSummary.toCharacterSummaryUin() : CharacterSummaryModel = CharacterSummaryModel(
    id = id,
    name = name,
    race = race,
    gender = gender,
    affiliation = affiliation,
    image = image,
    ki = ki,
    maxKi = maxKi
)
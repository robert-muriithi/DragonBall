package com.robert.character_details.mapper

import com.robert.character_details.model.CharacterDetailsModel
import com.robert.character_details.model.PlanetModel
import com.robert.character_details.model.TransformationModel
import com.robert.domain.model.CharacterDetails
import com.robert.domain.model.Planet
import com.robert.domain.model.Transformation
import kotlinx.collections.immutable.toImmutableList

fun Transformation.toTransformationUi(): TransformationModel = TransformationModel(
    id = id,
    name = name,
    image = image,
    ki = ki
)

fun Planet.toPlanetUi(): PlanetModel = PlanetModel(
    id = id,
    name = name,
    isDestroyed = isDestroyed,
    description = description,
    image = image
)


fun CharacterDetails.toCharacterDetailsUi() : CharacterDetailsModel = CharacterDetailsModel(
    id = id,
    name = name,
    race = race,
    gender = gender,
    affiliation = affiliation,
    description = description,
    image = image,
    ki = ki,
    maxKi = maxKi,
    status = status,
    originPlanet = originPlanet?.toPlanetUi(),
    transformations = transformations.map { it.toTransformationUi() }.toImmutableList()
)


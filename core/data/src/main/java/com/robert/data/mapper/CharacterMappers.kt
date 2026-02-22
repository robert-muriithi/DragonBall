package com.robert.data.mapper

import kotlinx.collections.immutable.toImmutableList
import com.robert.database.entity.CharacterDetailsEntity
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.CharacterWithDetails
import com.robert.database.entity.PlanetEntity
import com.robert.database.entity.TransformationEntity
import com.robert.domain.model.CharacterDetails
import com.robert.domain.model.CharacterSummary
import com.robert.domain.model.Planet
import com.robert.domain.model.Transformation
import com.robert.network.model.CharacterDetailsDto
import com.robert.network.model.CharacterDto
import com.robert.network.model.PlanetDto
import com.robert.network.model.TransformationDto


fun CharacterDto.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    race = race,
    gender = gender,
    affiliation = affiliation,
    image = image,
    ki = ki,
    maxKi = maxKi
)

fun CharacterDetailsDto.toDetailsEntity(): CharacterDetailsEntity = CharacterDetailsEntity(
    characterId = id,
    description = description,
    deletedAt = deletedAt
)

fun CharacterDetailsDto.toPlanetEntity(): PlanetEntity? = originPlanet?.let { planet ->
    PlanetEntity(
        characterId = id,
        planetId = planet.id,
        name = planet.name,
        isDestroyed = planet.isDestroyed,
        description = planet.description,
        image = planet.image
    )
}

fun CharacterDetailsDto.toTransformationEntities(): List<TransformationEntity> =
    transformations.map { t ->
        TransformationEntity(
            id = t.id,
            characterId = id,
            name = t.name,
            image = t.image,
            ki = t.ki
        )
    }


fun CharacterEntity.toDomainSummary(): CharacterSummary = CharacterSummary(
    id = id,
    name = name,
    race = race,
    gender = gender,
    affiliation = affiliation,
    image = image,
    ki = ki,
    maxKi = maxKi
)

fun CharacterWithDetails.toDomainDetails(): CharacterDetails = CharacterDetails(
    id = character.id,
    name = character.name,
    race = character.race,
    gender = character.gender,
    affiliation = character.affiliation,
    description = extras?.description,
    image = character.image,
    ki = character.ki,
    maxKi = character.maxKi,
    status = if (extras?.deletedAt == null) "Alive" else "Deceased",
    originPlanet = planet?.toDomain(),
    transformations = transformations.map { it.toDomain() }.toImmutableList()
)

fun PlanetEntity.toDomain(): Planet = Planet(
    id = planetId,
    name = name,
    isDestroyed = isDestroyed,
    description = description,
    image = image
)

fun TransformationEntity.toDomain(): Transformation = Transformation(
    id = id,
    name = name,
    image = image,
    ki = ki
)

fun PlanetDto.toDomain(): Planet = Planet(
    id = id,
    name = name,
    isDestroyed = isDestroyed,
    description = description,
    image = image
)

fun TransformationDto.toDomain(): Transformation = Transformation(
    id = id,
    name = name,
    image = image,
    ki = ki
)

fun CharacterDetailsDto.toDomainDetails(): CharacterDetails = CharacterDetails(
    id = id,
    name = name,
    race = race,
    gender = gender,
    affiliation = affiliation,
    description = description,
    image = image,
    ki = ki,
    maxKi = maxKi,
    status = if (deletedAt == null) "Alive" else "Deceased",
    originPlanet = originPlanet?.toDomain(),
    transformations = transformations.map { it.toDomain() }.toImmutableList()
)


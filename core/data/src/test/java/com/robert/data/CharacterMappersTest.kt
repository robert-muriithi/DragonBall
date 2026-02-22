package com.robert.data

import com.google.common.truth.Truth.assertThat
import kotlinx.collections.immutable.immutableListOf
import com.robert.data.mapper.toDomainDetails
import com.robert.data.mapper.toDomainSummary
import com.robert.data.mapper.toDetailsEntity
import com.robert.data.mapper.toEntity
import com.robert.data.mapper.toPlanetEntity
import com.robert.data.mapper.toTransformationEntities
import com.robert.database.entity.CharacterDetailsEntity
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.CharacterWithDetails
import com.robert.database.entity.PlanetEntity
import com.robert.database.entity.TransformationEntity
import com.robert.network.model.CharacterDetailsDto
import com.robert.network.model.CharacterDto
import com.robert.network.model.PlanetDto
import com.robert.network.model.TransformationDto
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class CharacterMappersTest {


    @Test
    fun `CharacterDto toEntity maps all fields correctly`() {
        val dto = characterDto()
        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo(1)
        assertThat(entity.name).isEqualTo("Goku")
        assertThat(entity.race).isEqualTo("Saiyan")
        assertThat(entity.gender).isEqualTo("Male")
        assertThat(entity.affiliation).isEqualTo("Z Fighter")
        assertThat(entity.image).isEqualTo("https://example.com/goku.png")
        assertThat(entity.ki).isEqualTo("1000")
        assertThat(entity.maxKi).isEqualTo("9000")
    }


    @Test
    fun `CharacterEntity toDomainSummary maps all fields correctly`() {
        val entity = characterEntity()
        val summary = entity.toDomainSummary()

        assertThat(summary.id).isEqualTo(1)
        assertThat(summary.name).isEqualTo("Goku")
        assertThat(summary.race).isEqualTo("Saiyan")
        assertThat(summary.gender).isEqualTo("Male")
        assertThat(summary.affiliation).isEqualTo("Z Fighter")
        assertThat(summary.ki).isEqualTo("1000")
        assertThat(summary.maxKi).isEqualTo("9000")
    }


    @Test
    fun `CharacterDetailsDto toDetailsEntity maps characterId and description`() {
        val dto = characterDetailsDto()
        val entity = dto.toDetailsEntity()

        assertThat(entity.characterId).isEqualTo(1)
        assertThat(entity.description).isEqualTo("A mighty Saiyan warrior")
        assertThat(entity.deletedAt).isNull()
    }

    @Test
    fun `CharacterDetailsDto toDetailsEntity maps deletedAt when present`() {
        val dto = characterDetailsDto(deletedAt = "2023-01-01")
        val entity = dto.toDetailsEntity()

        assertThat(entity.deletedAt).isEqualTo("2023-01-01")
    }

    @Test
    fun `CharacterDetailsDto toPlanetEntity returns null when originPlanet is null`() {
        val dto = characterDetailsDto(planet = null)
        assertThat(dto.toPlanetEntity()).isNull()
    }

    @Test
    fun `CharacterDetailsDto toPlanetEntity maps planet fields correctly`() {
        val dto = characterDetailsDto()
        val entity = dto.toPlanetEntity()!!

        assertThat(entity.characterId).isEqualTo(1)
        assertThat(entity.planetId).isEqualTo(10)
        assertThat(entity.name).isEqualTo("Earth")
        assertThat(entity.isDestroyed).isFalse()
    }

    @Test
    fun `CharacterDetailsDto toTransformationEntities maps all transformations`() {
        val dto = characterDetailsDto()
        val entities = dto.toTransformationEntities()

        assertThat(entities).hasSize(2)
        assertThat(entities[0].id).isEqualTo(100)
        assertThat(entities[0].characterId).isEqualTo(1)
        assertThat(entities[0].name).isEqualTo("Super Saiyan")
        assertThat(entities[1].id).isEqualTo(101)
        assertThat(entities[1].name).isEqualTo("Super Saiyan 2")
    }

    @Test
    fun `CharacterDetailsDto toTransformationEntities returns empty list when no transformations`() {
        val dto = characterDetailsDto(transformations = persistentListOf())
        assertThat(dto.toTransformationEntities()).isEmpty()
    }


    @Test
    fun `CharacterDetailsDto toDomainDetails status is Alive when deletedAt is null`() {
        val dto = characterDetailsDto(deletedAt = null)
        val domain = dto.toDomainDetails()
        assertThat(domain.status).isEqualTo("Alive")
    }

    @Test
    fun `CharacterDetailsDto toDomainDetails status is Deceased when deletedAt is set`() {
        val dto = characterDetailsDto(deletedAt = "2023-01-01")
        val domain = dto.toDomainDetails()
        assertThat(domain.status).isEqualTo("Deceased")
    }

    @Test
    fun `CharacterDetailsDto toDomainDetails maps planet and transformations`() {
        val dto = characterDetailsDto()
        val domain = dto.toDomainDetails()

        assertThat(domain.originPlanet).isNotNull()
        assertThat(domain.originPlanet!!.name).isEqualTo("Earth")
        assertThat(domain.transformations).hasSize(2)
    }


    @Test
    fun `CharacterWithDetails toDomainDetails status is Alive when deletedAt is null`() {
        val withDetails = characterWithDetails(deletedAt = null)
        val domain = withDetails.toDomainDetails()
        assertThat(domain.status).isEqualTo("Alive")
    }

    @Test
    fun `CharacterWithDetails toDomainDetails status is Deceased when deletedAt is set`() {
        val withDetails = characterWithDetails(deletedAt = "2023-01-01")
        val domain = withDetails.toDomainDetails()
        assertThat(domain.status).isEqualTo("Deceased")
    }

    @Test
    fun `CharacterWithDetails toDomainDetails maps null planet correctly`() {
        val withDetails = characterWithDetails(includePlanet = false)
        val domain = withDetails.toDomainDetails()
        assertThat(domain.originPlanet).isNull()
    }

    @Test
    fun `CharacterWithDetails toDomainDetails maps transformations correctly`() {
        val withDetails = characterWithDetails()
        val domain = withDetails.toDomainDetails()
        assertThat(domain.transformations).hasSize(2)
        assertThat(domain.transformations[0].name).isEqualTo("Super Saiyan")
    }


    private fun characterDto() = CharacterDto(
        id = 1, name = "Goku", race = "Saiyan", gender = "Male",
        affiliation = "Z Fighter", image = "https://example.com/goku.png",
        ki = "1000", maxKi = "9000", description = null, deletedAt = null
    )

    private fun characterEntity() = CharacterEntity(
        id = 1, name = "Goku", race = "Saiyan", gender = "Male",
        affiliation = "Z Fighter", image = null, ki = "1000", maxKi = "9000"
    )

    private fun characterDetailsDto(
        deletedAt: String? = null,
        planet: PlanetDto? = PlanetDto(10, "Earth", false, "Home", null),
        transformations: List<TransformationDto> = persistentListOf(
            TransformationDto(100, "Super Saiyan", null, "150000"),
            TransformationDto(101, "Super Saiyan 2", null, "200000"),
        )
    ) = CharacterDetailsDto(
        id = 1, name = "Goku", race = "Saiyan", gender = "Male",
        affiliation = "Z Fighter", image = null, ki = "1000", maxKi = "9000",
        description = "A mighty Saiyan warrior", deletedAt = deletedAt,
        originPlanet = planet, transformations = transformations
    )

    private fun characterWithDetails(
        deletedAt: String? = null,
        includePlanet: Boolean = true
    ) = CharacterWithDetails(
        character = characterEntity(),
        extras = CharacterDetailsEntity(1, "A mighty Saiyan warrior", deletedAt),
        planet = if (includePlanet) PlanetEntity(1, 10, "Earth", false, "Home", null) else null,
        transformations = persistentListOf(
            TransformationEntity(100, 1, "Super Saiyan", null, "150000"),
            TransformationEntity(101, 1, "Super Saiyan 2", null, "200000"),
        )
    )
}

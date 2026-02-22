package com.robert.data

import androidx.room.withTransaction
import kotlinx.collections.immutable.immutableListOf
import com.google.common.truth.Truth.assertThat
import com.robert.data.repository.CharacterRepositoryImpl
import com.robert.database.DragonBallDatabase
import com.robert.database.dao.CharacterDao
import com.robert.database.dao.CharacterDetailsDao
import com.robert.database.dao.CharacterRemoteKeysDao
import com.robert.database.dao.PlanetDao
import com.robert.database.dao.TransformationDao
import com.robert.database.entity.CharacterDetailsEntity
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.CharacterWithDetails
import com.robert.database.entity.PlanetEntity
import com.robert.database.entity.TransformationEntity
import com.robert.network.api.DragonBallApiService
import com.robert.network.model.CharacterDetailsDto
import com.robert.network.model.PlanetDto
import com.robert.network.model.TransformationDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class CharacterRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val api = mockk<DragonBallApiService>()
    private val database = mockk<DragonBallDatabase>(relaxed = true)
    private val characterDao = mockk<CharacterDao>(relaxed = true)
    private val characterDetailsDao = mockk<CharacterDetailsDao>(relaxed = true)
    private val planetDao = mockk<PlanetDao>(relaxed = true)
    private val transformationDao = mockk<TransformationDao>(relaxed = true)
    private val remoteKeysDao = mockk<CharacterRemoteKeysDao>(relaxed = true)

    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setup() {
        every { database.characterDao() } returns characterDao
        every { database.characterDetailsDao() } returns characterDetailsDao
        every { database.planetDao() } returns planetDao
        every { database.transformationDao() } returns transformationDao
        every { database.characterRemoteKeysDao() } returns remoteKeysDao

        mockkStatic("androidx.room.RoomDatabaseKt")
        coEvery { database.withTransaction<Unit>(any()) } coAnswers {
            (secondArg() as suspend () -> Unit)()
        }

        repository = CharacterRepositoryImpl(api, database)
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }


    @Test
    fun `getCharacterDetails returns cached data without API call when extras exist`() = runTest {
        val cached = characterWithDetails(hasExtras = true)
        coEvery { characterDao.getCharacterWithDetails(1) } returns cached

        val result = repository.getCharacterDetails(1)

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Goku")
        assertThat(result.description).isEqualTo("A mighty Saiyan warrior")
        coVerify(exactly = 0) { api.getCharacterById(any()) }
    }

    @Test
    fun `getCharacterDetails returns Alive status when deletedAt is null in cache`() = runTest {
        val cached = characterWithDetails(hasExtras = true, deletedAt = null)
        coEvery { characterDao.getCharacterWithDetails(1) } returns cached

        val result = repository.getCharacterDetails(1)
        assertThat(result.status).isEqualTo("Alive")
    }

    @Test
    fun `getCharacterDetails returns Deceased status when deletedAt is set in cache`() = runTest {
        val cached = characterWithDetails(hasExtras = true, deletedAt = "2023-01-01")
        coEvery { characterDao.getCharacterWithDetails(1) } returns cached

        val result = repository.getCharacterDetails(1)
        assertThat(result.status).isEqualTo("Deceased")
    }


    @Test
    fun `getCharacterDetails fetches from API when no cache`() = runTest {
        coEvery { characterDao.getCharacterWithDetails(1) } returns null
        coEvery { api.getCharacterById(1) } returns characterDetailsDto()
        coEvery { characterDetailsDao.upsert(any()) } just Runs
        coEvery { planetDao.upsert(any()) } just Runs
        coEvery { transformationDao.deleteByCharacterId(1) } just Runs
        coEvery { transformationDao.upsertAll(any()) } just Runs

        val result = repository.getCharacterDetails(1)

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Goku")
        coVerify(exactly = 1) { api.getCharacterById(1) }
    }

    @Test
    fun `getCharacterDetails persists details after network fetch`() = runTest {
        coEvery { characterDao.getCharacterWithDetails(1) } returns null
        coEvery { api.getCharacterById(1) } returns characterDetailsDto()
        coEvery { characterDetailsDao.upsert(any()) } just Runs
        coEvery { planetDao.upsert(any()) } just Runs
        coEvery { transformationDao.deleteByCharacterId(1) } just Runs
        coEvery { transformationDao.upsertAll(any()) } just Runs

        repository.getCharacterDetails(1)

        coVerify { characterDetailsDao.upsert(any()) }
        coVerify { planetDao.upsert(any()) }
        coVerify { transformationDao.deleteByCharacterId(1) }
        coVerify { transformationDao.upsertAll(any()) }
    }

    @Test
    fun `getCharacterDetails fetches from API when cache has no extras`() = runTest {
        val cachedNoExtras = characterWithDetails(hasExtras = false)
        coEvery { characterDao.getCharacterWithDetails(1) } returns cachedNoExtras
        coEvery { api.getCharacterById(1) } returns characterDetailsDto()
        coEvery { characterDetailsDao.upsert(any()) } just Runs
        coEvery { planetDao.upsert(any()) } just Runs
        coEvery { transformationDao.deleteByCharacterId(1) } just Runs
        coEvery { transformationDao.upsertAll(any()) } just Runs

        repository.getCharacterDetails(1)

        coVerify(exactly = 1) { api.getCharacterById(1) }
    }


    @Test
    fun `getCharacterDetails falls back to stale cache on IOException`() = runTest {
        val staleCache = characterWithDetails(hasExtras = false)
        coEvery { characterDao.getCharacterWithDetails(1) } returns staleCache
        coEvery { api.getCharacterById(1) } throws IOException("Network unavailable")

        val result = repository.getCharacterDetails(1)

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Goku")
    }

    @Test
    fun `getCharacterDetails throws when no cache and network fails`() = runTest {
        coEvery { characterDao.getCharacterWithDetails(1) } returns null
        coEvery { api.getCharacterById(1) } throws IOException("Network unavailable")

        var threw = false
        try {
            repository.getCharacterDetails(1)
        } catch (e: IOException) {
            threw = true
        }
        assertThat(threw).isTrue()
    }


    private fun characterWithDetails(
        hasExtras: Boolean = true,
        deletedAt: String? = null
    ) = CharacterWithDetails(
        character = CharacterEntity(1, "Goku", "Saiyan", "Male", "Z Fighter", null, "1000", "9000"),
        extras = if (hasExtras) CharacterDetailsEntity(1, "A mighty Saiyan warrior", deletedAt) else null,
        planet = PlanetEntity(1, 10, "Earth", false, "A great planet", null),
        transformations = persistentListOf(TransformationEntity(100, 1, "Super Saiyan", null, "150000"))
    )

    private fun characterDetailsDto() = CharacterDetailsDto(
        id = 1, name = "Goku", race = "Saiyan", gender = "Male",
        affiliation = "Z Fighter", image = null, ki = "1000", maxKi = "9000",
        description = "A mighty Saiyan warrior", deletedAt = null,
        originPlanet = PlanetDto(10, "Earth", false, "Home", null),
        transformations = persistentListOf(TransformationDto(100, "Super Saiyan", null, "150000"))
    )
}

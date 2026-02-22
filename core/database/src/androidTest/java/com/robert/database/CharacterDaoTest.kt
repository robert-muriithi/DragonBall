package com.robert.database

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.robert.database.dao.CharacterDao
import com.robert.database.entity.CharacterDetailsEntity
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.PlanetEntity
import com.robert.database.entity.TransformationEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {

    private lateinit var db: DragonBallDatabase
    private lateinit var dao: CharacterDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DragonBallDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.characterDao()
    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun upsertAll_insertsCharacters() = runTest {
        val characters = listOf(gokuEntity(), vegetaEntity())
        dao.upsertAll(characters)

        assertThat(dao.hasCharacters()).isTrue()
    }

    @Test
    fun upsertAll_replacesOnConflict() = runTest {
        dao.upsertAll(listOf(gokuEntity(affiliation = "Z Fighter")))
        dao.upsertAll(listOf(gokuEntity(affiliation = "Villain")))

        val result = dao.getCharacterWithDetails(1)
        assertThat(result?.character?.affiliation).isEqualTo("Villain")
    }


    @Test
    fun hasCharacters_returnsFalse_whenEmpty() = runTest {
        assertThat(dao.hasCharacters()).isFalse()
    }

    @Test
    fun hasCharacters_returnsTrue_afterInsert() = runTest {
        dao.upsertAll(listOf(gokuEntity()))
        assertThat(dao.hasCharacters()).isTrue()
    }


    @Test
    fun clearAll_removesAllCharacters() = runTest {
        dao.upsertAll(listOf(gokuEntity(), vegetaEntity()))
        dao.clearAll()
        assertThat(dao.hasCharacters()).isFalse()
    }


    @Test
    fun getCharacterWithDetails_returnsNull_whenNotFound() = runTest {
        val result = dao.getCharacterWithDetails(999)
        assertThat(result).isNull()
    }

    @Test
    fun getCharacterWithDetails_returnsCharacterWithNullExtras_whenNoDetails() = runTest {
        dao.upsertAll(listOf(gokuEntity()))

        val result = dao.getCharacterWithDetails(1)
        assertThat(result).isNotNull()
        assertThat(result!!.character.id).isEqualTo(1)
        assertThat(result.extras).isNull()
        assertThat(result.planet).isNull()
        assertThat(result.transformations).isEmpty()
    }

    @Test
    fun getCharacterWithDetails_returnsFullData_whenAllRelationsExist() = runTest {
        dao.upsertAll(listOf(gokuEntity()))
        db.characterDetailsDao().upsert(CharacterDetailsEntity(1, "A mighty Saiyan", null))
        db.planetDao().upsert(PlanetEntity(1, 100, "Earth", false, "Home planet", null))
        db.transformationDao().upsertAll(
            listOf(
                TransformationEntity(10, 1, "Super Saiyan", null, "150000"),
                TransformationEntity(11, 1, "Super Saiyan 2", null, "200000"),
            )
        )

        val result = dao.getCharacterWithDetails(1)!!
        assertThat(result.character.name).isEqualTo("Goku")
        assertThat(result.extras?.description).isEqualTo("A mighty Saiyan")
        assertThat(result.planet?.name).isEqualTo("Earth")
        assertThat(result.transformations).hasSize(2)
    }

    @Test
    fun characterDeletion_cascadesToRelatedEntities() = runTest {
        dao.upsertAll(listOf(gokuEntity()))
        db.characterDetailsDao().upsert(CharacterDetailsEntity(1, "desc", null))
        db.planetDao().upsert(PlanetEntity(1, 100, "Earth", false, null, null))

        dao.clearAll()

        val result = dao.getCharacterWithDetails(1)
        assertThat(result).isNull()
    }


    @Test
    fun pagingSource_withNoFilter_loadsAllCharacters() = runTest {
        dao.upsertAll(listOf(gokuEntity(), vegetaEntity(), frieza()))

        val source = dao.pagingSource(affiliation = null, searchQuery = null)
        val result = source.load(PagingSource.LoadParams.Refresh(null, 10, false))

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(3)
    }

    @Test
    fun pagingSource_withAffiliationFilter_returnsOnlyMatching() = runTest {
        dao.upsertAll(listOf(gokuEntity(), vegetaEntity(), frieza()))

        val source = dao.pagingSource(affiliation = "Villain", searchQuery = null)
        val result = source.load(PagingSource.LoadParams.Refresh(null, 10, false))

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(1)
        assertThat(page.data.first().name).isEqualTo("Frieza")
    }

    @Test
    fun pagingSource_withSearchQuery_returnsOnlyMatching() = runTest {
        dao.upsertAll(listOf(gokuEntity(), vegetaEntity(), frieza()))

        val source = dao.pagingSource(affiliation = null, searchQuery = "oku")
        val result = source.load(PagingSource.LoadParams.Refresh(null, 10, false))

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(1)
        assertThat(page.data.first().name).isEqualTo("Goku")
    }

    @Test
    fun pagingSource_withAffiliationAndSearch_narrowsResults() = runTest {
        dao.upsertAll(listOf(gokuEntity(), vegetaEntity(), frieza()))

        val source = dao.pagingSource(affiliation = "Z Fighter", searchQuery = "Goku")
        val result = source.load(PagingSource.LoadParams.Refresh(null, 10, false))

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(1)
        assertThat(page.data.first().name).isEqualTo("Goku")
    }

    @Test
    fun pagingSource_withNonMatchingFilter_returnsEmpty() = runTest {
        dao.upsertAll(listOf(gokuEntity(), vegetaEntity()))

        val source = dao.pagingSource(affiliation = "Frieza Force", searchQuery = null)
        val result = source.load(PagingSource.LoadParams.Refresh(null, 10, false))

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).isEmpty()
    }


    private fun gokuEntity(affiliation: String = "Z Fighter") = CharacterEntity(
        id = 1, name = "Goku", race = "Saiyan", gender = "Male",
        affiliation = affiliation, image = null, ki = "1000", maxKi = "9000"
    )

    private fun vegetaEntity() = CharacterEntity(
        id = 2, name = "Vegeta", race = "Saiyan", gender = "Male",
        affiliation = "Z Fighter", image = null, ki = "1200", maxKi = "9500"
    )

    private fun frieza() = CharacterEntity(
        id = 3, name = "Frieza", race = "Frieza Race", gender = "Male",
        affiliation = "Villain", image = null, ki = "530000", maxKi = "1000000"
    )
}

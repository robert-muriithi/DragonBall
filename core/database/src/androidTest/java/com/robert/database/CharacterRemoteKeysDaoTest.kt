package com.robert.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.robert.database.dao.CharacterRemoteKeysDao
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.CharacterRemoteKeysEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterRemoteKeysDaoTest {

    private lateinit var db: DragonBallDatabase
    private lateinit var dao: CharacterRemoteKeysDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DragonBallDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.characterRemoteKeysDao()
    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun upsertAll_insertsKeys() = runTest {
        seedCharacters()
        dao.upsertAll(listOf(remoteKey(characterId = 1, prevKey = null, nextKey = 2)))

        val result = dao.remoteKeysByCharacterId(1)
        assertThat(result).isNotNull()
        assertThat(result!!.nextKey).isEqualTo(2)
    }

    @Test
    fun upsertAll_replacesExistingKey() = runTest {
        seedCharacters()
        dao.upsertAll(listOf(remoteKey(1, null, 2)))
        dao.upsertAll(listOf(remoteKey(1, 1, 3)))

        val result = dao.remoteKeysByCharacterId(1)
        assertThat(result!!.nextKey).isEqualTo(3)
    }


    @Test
    fun remoteKeysByCharacterId_returnsNull_whenNotFound() = runTest {
        val result = dao.remoteKeysByCharacterId(999)
        assertThat(result).isNull()
    }

    @Test
    fun remoteKeysByCharacterId_returnsPrevKey() = runTest {
        seedCharacters()
        dao.upsertAll(listOf(remoteKey(1, prevKey = 1, nextKey = 3)))

        val result = dao.remoteKeysByCharacterId(1)
        assertThat(result!!.prevKey).isEqualTo(1)
    }


    @Test
    fun getNextPage_returnsNull_whenEmpty() = runTest {
        val result = dao.getNextPage()
        assertThat(result).isNull()
    }

    @Test
    fun getNextPage_returnsMaxNextKey() = runTest {
        seedCharacters()
        dao.upsertAll(
            listOf(
                remoteKey(1, null, 2),
                remoteKey(2, 1, 3),
                remoteKey(3, 2, null),
            )
        )

        val result = dao.getNextPage()
        assertThat(result).isEqualTo(3)
    }


    @Test
    fun clearAll_removesAllKeys() = runTest {
        seedCharacters()
        dao.upsertAll(listOf(remoteKey(1, null, 2), remoteKey(2, 1, 3)))
        dao.clearAll()

        assertThat(dao.remoteKeysByCharacterId(1)).isNull()
        assertThat(dao.remoteKeysByCharacterId(2)).isNull()
        assertThat(dao.getNextPage()).isNull()
    }


    private suspend fun seedCharacters() {
        db.characterDao().upsertAll(
            listOf(
                CharacterEntity(1, "Goku", "Saiyan", "Male", "Z Fighter", null, "1000", "9000"),
                CharacterEntity(2, "Vegeta", "Saiyan", "Male", "Z Fighter", null, "1200", "9500"),
                CharacterEntity(3, "Frieza", "Frieza Race", "Male", "Villain", null, "530000", "1000000"),
            )
        )
    }

    private fun remoteKey(characterId: Int, prevKey: Int?, nextKey: Int?) =
        CharacterRemoteKeysEntity(characterId, prevKey, nextKey)
}

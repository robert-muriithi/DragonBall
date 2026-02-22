package com.robert.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.CharacterWithDetails

@Dao
interface CharacterDao {

    @Query(
        """
        SELECT * FROM characters
        WHERE (:affiliation IS NULL OR affiliation = :affiliation)
          AND (:searchQuery IS NULL OR name LIKE '%' || :searchQuery || '%')
        ORDER BY id ASC
        """
    )
    fun pagingSource(
        affiliation: String?,
        searchQuery: String?
    ): PagingSource<Int, CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<CharacterEntity>)

    @Transaction
    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterWithDetails(id: Int): CharacterWithDetails?

    @Query("SELECT * FROM characters WHERE name LIKE :query ORDER BY name ASC LIMIT :limit")
    suspend fun searchByName(query: String, limit: Int): List<CharacterEntity>

    @Query("SELECT COUNT(*) > 0 FROM characters")
    suspend fun hasCharacters(): Boolean

    @Query("DELETE FROM characters")
    suspend fun clearAll()
}

package com.robert.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.robert.data.mapper.toEntity
import com.robert.database.DragonBallDatabase
import com.robert.database.entity.CharacterEntity
import com.robert.database.entity.CharacterRemoteKeysEntity
import com.robert.network.api.DragonBallApiService
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator(
    private val api: DragonBallApiService,
    private val database: DragonBallDatabase,
    private val searchQuery: String?
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun initialize(): InitializeAction {
        return if (database.characterDao().hasCharacters()) {
            Timber.d("Characters cached, skipping initial refresh")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        Timber.d("RemoteMediator load - type: $loadType, query: $searchQuery")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    if (hasSearchQuery()) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    // I use the max stored nextKey instead of lastItemOrNull() so that
                    // APPEND continues even when the active filter shows 0 visible items
                    database.characterRemoteKeysDao().getNextPage()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = when {
                hasSearchQuery() && loadType == LoadType.REFRESH ->
                    api.getCharacters(page = STARTING_PAGE, limit = SEARCH_PAGE_SIZE, name = searchQuery)
                else ->
                    api.getCharacters(page = page, limit = state.config.pageSize)
            }

            val responseItems = response.items
            val endReached = hasSearchQuery() || responseItems.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.characterRemoteKeysDao().clearAll()
                    database.characterDao().clearAll()
                }

                val prevKey = if (page == STARTING_PAGE) null else page - STARTING_PAGE
                val nextKey = if (endReached) null else page + STARTING_PAGE
                database.characterRemoteKeysDao().upsertAll(
                    responseItems.map {
                        CharacterRemoteKeysEntity(
                            characterId = it.id,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }
                )

                database.characterDao().upsertAll(responseItems.map { it.toEntity() })
            }

            Timber.d("RemoteMediator success - stored ${responseItems.size} items, endReached: $endReached")
            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: Exception) {
            Timber.e(e, "RemoteMediator error")
            MediatorResult.Error(e)
        }
    }

    private fun hasSearchQuery(): Boolean = !searchQuery.isNullOrBlank()

    companion object {
        private const val STARTING_PAGE = 1
        private const val SEARCH_PAGE_SIZE = 100
    }
}

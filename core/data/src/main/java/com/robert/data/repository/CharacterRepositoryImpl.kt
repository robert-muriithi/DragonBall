package com.robert.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.robert.common.error.HttpException
import retrofit2.HttpException as RetrofitHttpException
import com.robert.data.mapper.toDetailsEntity
import com.robert.data.mapper.toDomainDetails
import com.robert.data.mapper.toDomainSummary
import com.robert.data.mapper.toEntity
import com.robert.data.mapper.toPlanetEntity
import com.robert.data.mapper.toTransformationEntities
import com.robert.data.paging.CharactersRemoteMediator
import com.robert.database.DragonBallDatabase
import com.robert.domain.model.CharacterDetails
import com.robert.domain.model.CharacterSummary
import com.robert.domain.repository.CharacterRepository
import com.robert.network.api.DragonBallApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class CharacterRepositoryImpl @Inject constructor(
    private val api: DragonBallApiService,
    private val database: DragonBallDatabase
) : CharacterRepository {

    override fun getCharactersPaged(
        affiliation: String?,
        searchQuery: String?
    ): Flow<PagingData<CharacterSummary>> {
        Timber.d("Getting paged characters - affiliation: $affiliation, query: $searchQuery")
        val normalizedAffiliation = affiliation?.takeIf { it.isNotBlank() }
        val normalizedQuery = searchQuery?.takeIf { it.isNotBlank() }

        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                prefetchDistance = DEFAULT_PREFETCH_DISTANCE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = CharactersRemoteMediator(
                api = api,
                database = database,
                searchQuery = normalizedQuery
            ),
            pagingSourceFactory = {
                database.characterDao().pagingSource(
                    affiliation = normalizedAffiliation,
                    searchQuery = normalizedQuery
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomainSummary() }
        }
    }

    override suspend fun getCharacterDetails(characterId: Int): CharacterDetails {
        val cached = database.characterDao().getCharacterWithDetails(characterId)
        if (cached?.extras != null) {
            Timber.d("Returning cached details for character $characterId")
            return cached.toDomainDetails()
        }

        // Not cached yet so i fetch from API and persist
        return try {
            val detailsDto = api.getCharacterById(characterId)
            Timber.d("Successfully fetched character details: ${detailsDto.name}")

            database.withTransaction {
                database.characterDetailsDao().upsert(detailsDto.toDetailsEntity())
                detailsDto.toPlanetEntity()?.let { database.planetDao().upsert(it) }
                database.transformationDao().deleteByCharacterId(characterId)
                database.transformationDao().upsertAll(detailsDto.toTransformationEntities())
            }

            detailsDto.toDomainDetails()
        } catch (e: RetrofitHttpException) {
            Timber.e(e, "HTTP error fetching character $characterId, falling back to cache")
            cached?.toDomainDetails() ?: throw HttpException(e.code(), e.message())
        } catch (e: Exception) {
            Timber.e(e, "Error fetching character $characterId, falling back to cache")
            cached?.toDomainDetails() ?: throw e
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val DEFAULT_PREFETCH_DISTANCE = 5
        const val INITIAL_LOAD_SIZE = DEFAULT_PAGE_SIZE * 2
    }
}

package com.robert.network.api

import com.robert.network.model.CharacterDetailsDto
import com.robert.network.model.CharacterDto
import com.robert.network.model.PagedResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DragonBallApiService {

    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("name") name: String? = null
    ): PagedResponse<CharacterDto>

    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDetailsDto
}


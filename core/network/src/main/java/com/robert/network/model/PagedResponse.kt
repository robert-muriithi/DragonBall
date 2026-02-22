package com.robert.network.model

data class PagedResponse<T>(
    val items: List<T>,
    val meta: MetaDto
)


package com.robert.common.error


class HttpException(
    val code: Int,
    override val message: String? = null
) : Exception(message)


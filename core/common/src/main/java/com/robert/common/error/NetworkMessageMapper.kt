package com.robert.common.error

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkMessageMapper {

    fun getErrorMessage(throwable: Throwable): HTTPErrorMessage = when (throwable) {
        is UnknownHostException -> HTTPErrorMessage.NoInternet
        is SocketTimeoutException -> HTTPErrorMessage.TimeoutError
        is IOException -> HTTPErrorMessage.NetworkError
        is HttpException -> mapHttpException(throwable)
        else -> HTTPErrorMessage.UnknownError
    }

    private fun mapHttpException(exception: HttpException): HTTPErrorMessage = when (exception.code) {
        400 -> HTTPErrorMessage.BadRequest
        401 -> HTTPErrorMessage.Unauthorized
        403 -> HTTPErrorMessage.Forbidden
        404 -> HTTPErrorMessage.NotFound
        408 -> HTTPErrorMessage.TimeoutError
        in 500..599 -> HTTPErrorMessage.ServerError
        else -> HTTPErrorMessage.Generic
    }
}

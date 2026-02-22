package com.robert.common.error

import com.robert.common.constants.ErrorMessages
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object ErrorHandler {
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> ErrorMessages.NO_INTERNET
            is SocketTimeoutException -> ErrorMessages.TIMEOUT_ERROR
            is IOException -> ErrorMessages.NETWORK_ERROR
            is HttpException -> mapHttpException(throwable)
            else -> throwable.message ?: ErrorMessages.UNKNOWN_ERROR
        }
    }
    private fun mapHttpException(exception: HttpException): String {
        return when (exception.code) {
            400 -> "Bad request. Please try again."
            401 -> "Unauthorized. Please log in again."
            403 -> "Access forbidden."
            404 -> ErrorMessages.NOT_FOUND
            408 -> ErrorMessages.TIMEOUT_ERROR
            in 500..599 -> ErrorMessages.SERVER_ERROR
            else -> ErrorMessages.GENERIC_ERROR
        }
    }

}



package com.robert.common.error

import androidx.annotation.StringRes
import com.robert.common.R


sealed class HTTPErrorMessage(@field:StringRes open val resourceId: Int) {

    data class Specified(@field:StringRes override val resourceId: Int) : HTTPErrorMessage(resourceId = resourceId)
    object Generic : HTTPErrorMessage(resourceId = R.string.error_generic)
    object NetworkError : HTTPErrorMessage(resourceId = R.string.error_network)
    object ServerError : HTTPErrorMessage(resourceId = R.string.error_server)
    object NotFound : HTTPErrorMessage(resourceId = R.string.error_not_found)
    object TimeoutError : HTTPErrorMessage(resourceId = R.string.error_timed_out)
    object NoInternet : HTTPErrorMessage(resourceId = R.string.error_no_internet)
    object UnknownError : HTTPErrorMessage(resourceId = R.string.error_unknown)
    object Unauthorized : HTTPErrorMessage(resourceId = R.string.error_unauthorized)
    object BadRequest : HTTPErrorMessage(resourceId = R.string.error_bad_request)
    object Forbidden : HTTPErrorMessage(resourceId = R.string.error_forbidden)
}

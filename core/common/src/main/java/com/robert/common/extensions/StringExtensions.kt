package com.robert.common.extensions

/**
 * Returns the string if not null or blank, otherwise returns the default value.
 */
fun String?.orDefault(default: String = ""): String {
    return if (this.isNullOrBlank()) default else this
}

/**
 * Returns "Unknown" if string is null or blank.
 */
fun String?.orUnknown(): String = orDefault("Unknown")

/**
 * Returns "-" if string is null or blank.
 */
fun String?.orDash(): String = orDefault("-")

/**
 * Capitalizes the first letter of the string.
 */
fun String.capitalizeFirst(): String {
    return if (isEmpty()) this else this[0].uppercase() + substring(1)
}

/**
 * Truncates the string to the specified max length with ellipsis.
 */
fun String.truncate(maxLength: Int, ellipsis: String = "..."): String {
    return if (length <= maxLength) this else take(maxLength - ellipsis.length) + ellipsis
}


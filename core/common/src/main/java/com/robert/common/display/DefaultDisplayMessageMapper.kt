package com.robert.common.display


object DefaultDisplayMessageMapper {

    /**
     * Returns the string if not null or blank, otherwise returns the default value.
     */
    fun default(value: String?, fallback: String = ""): String =
        if (value.isNullOrBlank()) fallback else value

    /**
     * Returns "Unknown" if string is null or blank.
     */
    fun unknown(value: String?): String = default(value, "Unknown")

    /**
     * Returns "-" if string is null or blank.
     */
    fun dash(value: String?): String = default(value, "-")
}

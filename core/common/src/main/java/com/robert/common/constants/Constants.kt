package com.robert.common.constants



object Affiliations {
    const val Z_FIGHTER = "Z Fighter"
    const val ARMY_OF_FRIEZA = "Army of Frieza"
    const val RED_RIBBON_ARMY = "Red Ribbon Army"
    const val PRIDE_TROOPERS = "Pride Troopers"
    const val FREELANCER = "Freelancer"
    const val VILLAIN = "Villain"
    const val ASSISTANT_OF_BEERUS = "Assistant of Beerus"

    const val NAMEKIAN_WARRIOR = "Namekian Warrior"
    const val OTHER = "Other"
    const val ASSISTANT_OF_VERMOUD = "Assistant of Vermoud"
    const val GOD = "God"


    val ALL = listOf(
        Z_FIGHTER,
        VILLAIN,
        FREELANCER,
        ARMY_OF_FRIEZA,
        GOD,
        NAMEKIAN_WARRIOR,
        ASSISTANT_OF_BEERUS,
        ASSISTANT_OF_VERMOUD,
        RED_RIBBON_ARMY,
        PRIDE_TROOPERS,
        OTHER
    )
}

object ErrorMessages {
    const val GENERIC_ERROR = "Something went wrong. Please try again."
    const val NETWORK_ERROR = "Network error. Please check your connection."
    const val SERVER_ERROR = "Server error. Please try again later."
    const val NOT_FOUND = "Resource not found."
    const val TIMEOUT_ERROR = "Request timed out. Please try again."
    const val NO_INTERNET = "No internet connection."
    const val UNKNOWN_ERROR = "An unknown error occurred."
}


package com.robert.common.constants

enum class Affiliations(val value: String) {
    Z_FIGHTER("Z Fighter"),
    ARMY_OF_FRIEZA("Army of Frieza"),
    RED_RIBBON_ARMY("Red Ribbon Army"),
    PRIDE_TROOPERS("Pride Troopers"),
    FREELANCER("Freelancer"),
    VILLAIN("Villain"),
    ASSISTANT_OF_BEERUS("Assistant of Beerus"),
    NAMEKIAN_WARRIOR("Namekian Warrior"),
    OTHER("Other"),
    ASSISTANT_OF_VERMOUD("Assistant of Vermoud"),
    GOD("God");

    companion object {
        val all: List<Affiliations> = entries.toList()
    }
}

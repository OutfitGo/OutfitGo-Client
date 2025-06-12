package com.outfitgo.store.core.util.enums

enum class SortOption {
    ALPHABETICAL,
    ALPHABETICAL_REVERSED,
    LOWEST_PRICE,
    HIGHEST_PRICE;

    fun getName(): String {
        return when (this) {
            ALPHABETICAL -> "A-Z"
            ALPHABETICAL_REVERSED -> "Z-A"
            LOWEST_PRICE -> "Lowest Price"
            HIGHEST_PRICE -> "Highest Price"
        }
    }
}
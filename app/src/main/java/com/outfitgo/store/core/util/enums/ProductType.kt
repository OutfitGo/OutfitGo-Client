package com.outfitgo.store.core.util.enums

enum class ProductType {
    ALL,
    SHOES,
    ACCESSORIES,
    T_SHIRTS;

    fun getName(): String {
        return when (this) {
            ALL -> "All"
            SHOES -> "Shoes"
            ACCESSORIES -> "Accessories"
            T_SHIRTS -> "T-Shirts"
        }
    }
}
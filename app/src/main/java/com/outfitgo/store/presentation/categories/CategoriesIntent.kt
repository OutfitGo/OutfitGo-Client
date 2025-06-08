package com.outfitgo.store.presentation.categories

import com.outfitgo.store.domain.model.Collection

sealed interface CategoriesIntent {
    data class GoToCategoryProducts(val category: Collection) : CategoriesIntent
}
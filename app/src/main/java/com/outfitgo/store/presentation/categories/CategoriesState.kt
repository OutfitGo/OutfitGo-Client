package com.outfitgo.store.presentation.categories

import com.outfitgo.store.R
import com.outfitgo.store.domain.model.Collection

data class CategoriesState(
    val categories: List<Collection> = emptyList(),
    val isLoading: Boolean = true
)

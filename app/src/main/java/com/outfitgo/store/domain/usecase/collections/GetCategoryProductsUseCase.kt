package com.outfitgo.store.domain.usecase.collections

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import javax.inject.Inject

class GetCategoryProductsUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepository
) {
    suspend fun execute(
        categoryHandle: String
    ): List<Product> {
        return collectionsRepository.fetchCategoryCollectionProducts(categoryHandle = categoryHandle)
    }
}
package com.outfitgo.store.domain.usecase.collections

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import javax.inject.Inject

class GetBrandProductsUseCase @Inject constructor(
    private val brandsRepository: CollectionsRepository
) {
    suspend fun execute(
        brand: String,
        productName: String,
        first: Int,
        after: String?
    ): List<Product> = brandsRepository.fetchBrandCollectionProducts(
        brand = brand,
        productName = productName,
        first = first,
        after = after
    )
}
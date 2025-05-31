package com.outfitgo.store.domain.usecase.brands

import com.outfitgo.store.domain.repository.brand.BrandsRepository
import javax.inject.Inject

class GetBrandProductsUseCase @Inject constructor(
    private val brandsRepository: BrandsRepository
) {
    suspend fun execute(
        brand: String,
        searchQuery: String,
        first: Int,
        after: String?
    ) = brandsRepository.fetchBrandProducts(
        brand = brand,
        searchQuery = searchQuery,
        first = first,
        after = after
    )
}
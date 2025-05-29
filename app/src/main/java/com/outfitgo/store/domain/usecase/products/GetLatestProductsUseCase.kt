package com.outfitgo.store.domain.usecase.products

import com.outfitgo.store.domain.repository.product.ProductsRepository
import javax.inject.Inject

class GetLatestProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend fun execute(
        first: Int,
        after: String?
    ) = productsRepository.fetchLatestProducts(first = first, after = after)
}
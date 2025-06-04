package com.outfitgo.store.domain.usecase.products

import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.repository.product.ProductsRepository
import javax.inject.Inject

class SearchProductByTitleUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    suspend fun execute(title: String): List<CommonProduct> =
        productsRepository.searchProductsByTitle(title)

}
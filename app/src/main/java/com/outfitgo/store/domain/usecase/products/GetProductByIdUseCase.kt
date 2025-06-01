package com.outfitgo.store.domain.usecase.products

import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.domain.repository.product.ProductsRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend fun execute(id: String): DetailedProduct = productsRepository.fetchProductById(id)
}
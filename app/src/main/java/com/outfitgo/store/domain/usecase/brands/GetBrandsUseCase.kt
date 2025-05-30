package com.outfitgo.store.domain.usecase.brands

import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.repository.brand.BrandsRepository
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(
    private val brandsRepository: BrandsRepository
) {
    suspend fun execute(
        first: Int,
        after: String?
    ):List<Brand> = brandsRepository.fetchAllBrands(first = first, after = after)
}
package com.outfitgo.store.domain.usecase.collections

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepository
) {
    suspend fun execute(): List<Collection> = collectionsRepository.fetchCollections().takeLast(4)
}
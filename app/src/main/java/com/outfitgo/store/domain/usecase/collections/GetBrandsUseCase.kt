package com.outfitgo.store.domain.usecase.collections

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepository
) {
    suspend fun execute(): List<Collection> = collectionsRepository
        .fetchCollections()
        .drop(1) //First collection is HomeScreen which is a not valid collection
        .dropLast(4) //Last 4 collections are categories not collections
}
package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.repository.cart.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartIdUseCase @Inject constructor(private val repository: CartRepository) {
    suspend fun execute(): Flow<String> {
        return repository.getCartId()
    }
}
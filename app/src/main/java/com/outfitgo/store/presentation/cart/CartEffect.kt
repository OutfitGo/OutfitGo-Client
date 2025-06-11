package com.outfitgo.store.presentation.cart

sealed interface CartEffect {
    data class ShowRemoveItemWarning(val lineId: String) : CartEffect
}
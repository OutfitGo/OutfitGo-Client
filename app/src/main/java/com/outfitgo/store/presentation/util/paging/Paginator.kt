package com.outfitgo.store.presentation.util.paging

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}
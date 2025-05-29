package com.outfitgo.store.presentation.util.paging


class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> List<Item>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (items: List<Item>) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }

        isMakingRequest = true
        onLoadUpdated(true)

        try {
            val result = onRequest(currentKey)
            isMakingRequest = false
            currentKey = getNextKey(result)
            onSuccess(result)
        }catch (exception: Exception){
            onError(exception)
        }

        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}


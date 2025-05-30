package com.outfitgo.store.presentation.util.paging

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> List<Item>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (items: List<Item>) -> Unit,
    private val isEndReached: suspend (List<Item>) -> Boolean
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false
    private var endReached = false

    override suspend fun loadNextItems() {
        if (isMakingRequest || endReached) {
            return
        }

        isMakingRequest = true
        onLoadUpdated(true)

        try {
            val result = onRequest(currentKey)
            endReached = isEndReached(result) // update based on items returned
            currentKey = getNextKey(result)
            onSuccess(result)
        } catch (exception: Exception) {
            onError(exception)
        }

        onLoadUpdated(false)
        isMakingRequest = false
    }

    override fun reset() {
        currentKey = initialKey
        endReached = false
    }
}



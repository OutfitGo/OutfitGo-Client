package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.storefront.BrandsQuery

fun BrandsQuery.Edge.toBrand(): Brand{
    return Brand(
        id = this.node.id,
        name = this.node.title,
        imageUrl = this.node.image?.url.toString(),
        pageCursor = this.cursor
    )
}
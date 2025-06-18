package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.cart.BuyerIdentity
import com.outfitgo.store.domain.model.cart.Cart
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.cart.Cost
import com.outfitgo.store.domain.model.cart.Customer
import com.outfitgo.store.domain.model.cart.DiscountCode
import com.outfitgo.store.domain.model.cart.Merchandise
import com.outfitgo.store.storefront.AddBuyerToCartMutation
import com.outfitgo.store.storefront.AddItemToCartMutation
import com.outfitgo.store.storefront.ApplyCouponToCartMutation
import com.outfitgo.store.storefront.GetCartQuery
import com.outfitgo.store.storefront.RemoveItemFromCartMutation
import com.outfitgo.store.storefront.UpdateCartLineQuantityMutation
import kotlin.collections.firstOrNull
import kotlin.collections.mapNotNull

fun GetCartQuery.Cart.toDomain(): Cart {
    return Cart(
        id = id,
        totalQuantity = totalQuantity,
        buyerIdentity = buyerIdentity.toDomain(),
        items = lines.edges.map { it.node.toDomain() },
        discountCode = discountCodes.firstOrNull()?.toDomain() ?: DiscountCode("", false),
        cost = Cost(
            totalAmount = cost.totalAmount.amount.toString(),
            subtotalAmount = cost.subtotalAmount.amount.toString(),
            discountedAmount = discountAllocations.sumOf { it.discountedAmount.amount.toString().toDouble() }.toString() ?: "0"

        ),
        checkoutUrl = checkoutUrl.toString()
    )
}

fun GetCartQuery.BuyerIdentity.toDomain(): BuyerIdentity {
    return BuyerIdentity(
        customer = customer?.toDomain() ?: Customer("")
    )
}

fun GetCartQuery.Customer.toDomain(): Customer {
    return Customer(
        email = email ?: ""
    )
}

fun GetCartQuery.Node.toDomain(): CartItem {
    return CartItem(
        id = id,
        quantity = quantity,
        merchandise = (merchandise as? GetCartQuery.Merchandise)?.toDomain() ?: Merchandise(
            "",
            "",
            "",
            ""
        )
    )
}

fun GetCartQuery.Merchandise.toDomain(): Merchandise {
    return Merchandise(
        title = onProductVariant?.title ?: "",
        price = "${onProductVariant?.price?.amount ?: "0.0"}",
        img = "${onProductVariant?.product?.featuredImage?.url ?: ""}",
        variantId = onProductVariant?.id ?: ""
    )
}

fun GetCartQuery.DiscountCode.toDomain(): DiscountCode {
    return DiscountCode(
        code = code,
        applicable = applicable
    )
}


fun AddBuyerToCartMutation.BuyerIdentity.toDomain(): BuyerIdentity {
    return BuyerIdentity(Customer(this.customer?.email ?: ""))
}

fun ApplyCouponToCartMutation.Cart.toDomain(): Cart {
    return Cart(
        id,
        null,
        null,
        null,
        this.discountCodes[0].toDomain(),
        Cost(
            "${this.cost.totalAmount.amount}",
            "${this.cost.subtotalAmount.amount}",
            "${this.discountAllocations.firstOrNull()?.discountedAmount?.amount?:0.00}"
        ),
        ""
    )
}


fun ApplyCouponToCartMutation.DiscountCode.toDomain(): DiscountCode {
    return DiscountCode(this.code, this.applicable)
}


fun UpdateCartLineQuantityMutation.Cart.toDomain(): Cost {
    return Cost(
        "${this.cost.totalAmount.amount}",
        "${this.cost.subtotalAmount.amount}",
        "${this.discountAllocations.firstOrNull()?.discountedAmount?.amount?:0.00}"
    )
}


fun AddItemToCartMutation.Cart.toDomain(): Cost {
    return Cost(
        "${this.cost.totalAmount.amount}",
        "${this.cost.subtotalAmount.amount}",
        "${this.discountAllocations.firstOrNull()?.discountedAmount?.amount?:0.00}"
    )
}

fun RemoveItemFromCartMutation.Cart.toDomain(): Cost {
    return Cost(
        "${this.cost.totalAmount.amount}",
        "${this.cost.subtotalAmount.amount}",
        "${this.discountAllocations.firstOrNull()?.discountedAmount?.amount?:0.00}"
    )
}

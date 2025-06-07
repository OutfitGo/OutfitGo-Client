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
        discountCode = discountCodes?.firstOrNull()?.toDomain() ?: DiscountCode("", false),
        cost = cost?.totalAmount?.toDomain() ?: Cost("")
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
        merchandise = (merchandise as? GetCartQuery.Merchandise)?.toDomain() ?: Merchandise("", "")
    )
}

fun GetCartQuery.Merchandise.toDomain(): Merchandise {
    return Merchandise(
        title = onProductVariant?.title ?: "",
        price = "${onProductVariant?.price?.amount ?: "0.0"}"
    )
}

fun GetCartQuery.DiscountCode.toDomain(): DiscountCode {
    return DiscountCode(
        code = code,
        applicable = applicable
    )
}

fun GetCartQuery.TotalAmount.toDomain(): Cost {
    return Cost(
        totalAmount = "$amount"
    )
}

fun AddBuyerToCartMutation.BuyerIdentity.toDomain(): BuyerIdentity {
    return BuyerIdentity(Customer(this.customer?.email ?: ""))
}

fun ApplyCouponToCartMutation.Cart.toDomain(): Cart {
    return Cart(id, null, null, null, this.discountCodes[0].toDomain(), this.cost.toDomain())
}

fun ApplyCouponToCartMutation.Cost.toDomain(): Cost {
    return Cost("${this.totalAmount.amount}")
}

fun ApplyCouponToCartMutation.DiscountCode.toDomain(): DiscountCode {
    return DiscountCode(this.code, this.applicable)
}

fun UpdateCartLineQuantityMutation.Cart.toDomain(): Cart {
    return Cart(id, null, null, null, null, this.cost.toDomain())
}

fun UpdateCartLineQuantityMutation.Cost.toDomain(): Cost {
    return Cost("${this.totalAmount.amount}")
}

fun AddItemToCartMutation.Cart.toDomain(): Cart {
    return Cart(null, null, null, null, null, this.cost.toDomain())
}

fun AddItemToCartMutation.Cost.toDomain(): Cost {
    return Cost("${this.totalAmount.amount}")
}

fun RemoveItemFromCartMutation.Cart.toDomain():Cart {
    return Cart(null, null, null, null, null, this.cost.toDomain())
}
fun RemoveItemFromCartMutation.Cost.toDomain():Cost{
    return Cost("${this.totalAmount.amount}")
}
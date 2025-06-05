package com.outfitgo.store.domain.model.cart

data class Cart(
    val id:String,
    val totalQuantity:Int,
    val buyerIdentity:BuyerIdentity,
    val items: List<CartItem>,
    val discountCode: DiscountCode,
    val cost:Cost

)
data class BuyerIdentity(
    val customer:Customer
)
data class Customer(
    val email:String
)
data class CartItem(
    val id: String,
    val quantity:Int,
    val merchandise:Merchandise
)
data class Merchandise(
    val title:String,
    val price:String
)
data class DiscountCode(
    val code:String,
    val applicable:Boolean
)
data class Cost(
    val totalAmount:String
)
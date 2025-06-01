package com.outfitgo.store.data.repository.user

import com.outfitgo.store.data.datasource.remote.user.LoginResponse
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.storefront.GetUserByAccessTokenQuery
import com.outfitgo.store.storefront.LoginMutation

fun GetUserByAccessTokenQuery.Customer.toUserModel(): User {
    return User(
        id = this.id,
        firstname = this.firstName ?: "FIRST",
        lastname = this.lastName ?: "LAST",
        displayName = this.displayName,
        email = this.email ?: "EMAIL",
    )
}

fun LoginMutation.CustomerAccessTokenCreate?.toLoginResponse(): LoginResponse {
    return LoginResponse(
        token = this?.customerAccessToken?.accessToken ?: "",
        errors = this?.customerUserErrors?.map { it.message } ?: emptyList()
    )
}

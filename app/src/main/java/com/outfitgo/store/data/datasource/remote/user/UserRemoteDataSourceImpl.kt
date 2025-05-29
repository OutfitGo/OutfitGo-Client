package com.outfitgo.store.data.datasource.remote.user

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.repository.user.toLoginResponse
import com.outfitgo.store.data.repository.user.toUserModel
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.storefront.GetUserByAccessTokenQuery
import com.outfitgo.store.storefront.LoginMutation
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    @StorefrontApollo private val client: ApolloClient
): UserRemoteDataSource {

    override suspend fun loginByEmailAndPassword(email: String, password: String): LoginResponse {
        val mutation = LoginMutation(email, password)
        val response = client.mutation(mutation).execute()

        if(response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }

        val data = response.dataAssertNoErrors.customerAccessTokenCreate
        return data.toLoginResponse()
    }

    override suspend fun getUserByAccessToken(token: String): User? {
        val query = GetUserByAccessTokenQuery(token)
        val response = client.query(query).execute()
        if(response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data?.customer
        return data?.toUserModel()
    }
}


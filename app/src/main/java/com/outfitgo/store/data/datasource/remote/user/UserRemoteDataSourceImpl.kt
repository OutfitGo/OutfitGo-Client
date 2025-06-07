package com.outfitgo.store.data.datasource.remote.user

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.repository.user.toLoginResponse
import com.outfitgo.store.data.repository.user.toUserModel
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.storefront.CreateNewCustomerMutation
import com.outfitgo.store.storefront.GetUserByAccessTokenQuery
import com.outfitgo.store.storefront.LoginMutation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "UserRemoteDataSourceImp"
class UserRemoteDataSourceImpl @Inject constructor(
    @StorefrontApollo private val client: ApolloClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRemoteDataSource {

    override suspend fun loginByEmailAndPassword(email: String, password: String): LoginResponse =
        withContext(dispatcher) {
            val mutation = LoginMutation(email, password)
            val response = client.mutation(mutation).execute()
            val errors = response.data?.customerAccessTokenCreate?.customerUserErrors

            if (response.hasErrors()) {
                throw Exception(response.errors?.first()?.message)
            }

            if(errors?.isNotEmpty() == true) {
                val msg = errors.first().message
                throw Exception(msg)
            }

            val data = response.dataAssertNoErrors.customerAccessTokenCreate
            data.toLoginResponse()
        }

    override suspend fun getUserByAccessToken(token: String): User? = withContext(dispatcher) {
        val query = GetUserByAccessTokenQuery(token)
        val response = client.query(query).execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data?.customer
        data?.toUserModel()
    }

    override suspend fun registerNewUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User {
        val mutation = CreateNewCustomerMutation(firstName, lastName, email, password)
        val response = client.mutation(mutation).execute()
        if(response.hasErrors()) {
            throw Exception("Couldn't make the Registration")
        }
        val errors = response.data?.customerCreate?.customerUserErrors ?: emptyList()
        if(errors.isNotEmpty()){
            val msg = errors.first().message
            throw Exception(msg)
        }
        val data = response.dataAssertNoErrors.customerCreate?.customer ?: throw Exception("Couldn't find the ID")
        val user = User(
            id = data.id,
            firstname = data.firstName ?: "firstname",
            lastname = data.lastName ?: "lastname",
            displayName = data.displayName,
            email = data.email ?: ""
        )
        return user
    }
}


package com.azamovhudstc.bookappwithcache.repo.impl

import com.azamovhudstc.bookappwithcache.data.local.database.appDatabase.AppDatabase
import com.azamovhudstc.bookappwithcache.data.local.sharedpref.AppReference
import com.azamovhudstc.bookappwithcache.data.remote.request.auth.AuthRequest
import com.azamovhudstc.bookappwithcache.data.remote.request.auth.VerifyRequest
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.cilent.ApiClient
import com.azamovhudstc.bookappwithcache.repo.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl : AuthRepository {
    private val authApi = ApiClient.getAuthApi()


    override fun login(
        password: String,
        phone: String
    ): Flow<Result<Unit>> = flow<Result<Unit>> {
        val response = authApi.login(AuthRequest.LoginRequest(password, phone))
        if (response.isSuccessful) {
            response.body()?.let {
                val token: String = response.body()!!.token
                AppReference.getInstance().verifyToken = token!!
                emit(Result.success(Unit))
            }
        } else {
            emit(Result.failure(Exception(response.errorBody()?.toString())))
        }
    }.catch {
        emit(Result.failure(Exception(it.message)))
    }.flowOn(Dispatchers.IO)

    override  fun register(
        name: String,
        password: String,
        phone: String,
        lastName: String
    ) = flow {
        var response =
            authApi.register(AuthRequest.RegisterRequest(name, lastName, password, phone))
        if (response.isSuccessful) {
            if (response.body() != null) {
                val token: String = response.body()!!.token
                AppReference.getInstance().verifyToken = token!!
                emit(true)
            }
            emit(false)
        }
        emit(false)

    }.flowOn(Dispatchers.IO)

    override fun verify(token: String, phone: String) = flow {
        var response = authApi.verify("Bearer $token", VerifyRequest(phone))
        if (response.isSuccessful && response.body() != null) {
            val responseToken: String = response.body()!!.token
            AppReference.getInstance().setToken(responseToken)
            emit(true)
        }

        emit(false)
    }.flowOn(Dispatchers.IO)

    override fun verifySign(token: String, phone: String): Flow<Boolean> = flow {
        var response = authApi.verifySign("Bearer $token", VerifyRequest(phone))
        if (response.isSuccessful && response.body() != null) {
            val token: String = response.body()!!.token
            AppReference.getInstance().setToken(token)
            emit(true)

        }

        emit(false)
    }.flowOn(Dispatchers.IO)

}
package com.azamovhudstc.bookappwithcache.data.remote.retrofit2.cilent

import com.azamovhudstc.bookappwithcache.app.App
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.service.auth.AuthService
import com.azamovhudstc.bookappwithcache.data.remote.retrofit2.service.book.BookService
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val myClient = OkHttpClient.Builder()
        .addInterceptor(ChuckInterceptor(App.instance))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://143.198.48.222:82")
        .client(myClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getAuthApi(): AuthService = retrofit.create(AuthService::class.java)
    fun getBookApi(): BookService = retrofit.create(BookService::class.java)


}


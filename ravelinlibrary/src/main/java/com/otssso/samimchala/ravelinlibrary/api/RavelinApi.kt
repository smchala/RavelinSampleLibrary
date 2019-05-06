package com.otssso.samimchala.ravelinlibrary.api

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RavelinApi {

    //could create a client and pass through the headers for every call! then set it in retrofit
    private val BASE_URL = "https://api-staging.ravelin.com/"

    private lateinit var customerApi: CustomerApi

    fun getRavelinClient(context: Context): CustomerApi {

        val clientBuilder = OkHttpClient.Builder()
        val headerAuthorizationInterceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                var request = chain.request()
                val headers = request.headers().newBuilder()
                    .add("Content-Type", "application/json;charset=utf-8").build()
                request = request.newBuilder().headers(headers).build()
                return chain.proceed(request)
            }
        }
        clientBuilder.addInterceptor(headerAuthorizationInterceptor)

        val client = clientBuilder.build()

        customerApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
//            .client(client)
            .build()
            .create(CustomerApi::class.java)

        return customerApi
    }
}
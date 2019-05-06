package com.otssso.samimchala.ravelinlibrary.api

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Api {

    private val BASE_URL = "https://api-staging.ravelin.com/"

    private var retrofit: Retrofit? = null

    fun getClient(context: Context): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
        }
        return retrofit
    }
}
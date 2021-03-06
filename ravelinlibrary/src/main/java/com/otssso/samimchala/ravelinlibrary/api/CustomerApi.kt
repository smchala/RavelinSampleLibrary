package com.otssso.samimchala.ravelinlibrary.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface CustomerApi {
    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("v2/customer")
    fun sendBlob(
        @Body blob: String,
        @Header("Authorization") token:String = "token secret_key_live_1Od41KPkFXqn9gR4KB8s5l3hvOaGAYqs"//, live key should be in build config file
    ): Single<retrofit2.Response<String>>
}
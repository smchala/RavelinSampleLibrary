package com.otssso.samimchala.ravelinlibrary.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header


interface CustomerApi {
    @POST("v2/customer")
    fun sendBlob(
        @Header("Authorization") token: String,
        @Body blob: String
    ): Call<ResponseBody>
}
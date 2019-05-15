package com.otssso.samimchala.ravelinlibrary.api

import com.otssso.samimchala.ravelinlibrary.BuildConfig
import okhttp3.*
import java.io.IOException


class FakeInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val response: Response?
        if (BuildConfig.DEBUG) {
            val responseString: String
            val uri = chain.request().url().uri()
            val query = uri.query
            val parsedQuery = query.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            responseString =
                if (parsedQuery[0].equals("id", ignoreCase = true) && parsedQuery[1].equals("1", ignoreCase = true)) {
                    TEACHER_ID_1
                } else if (parsedQuery[0].equals("id", ignoreCase = true) && parsedQuery[1].equals(
                        "2",
                        ignoreCase = true
                    )
                ) {
                    TEACHER_ID_2
                } else {
                    ""
                }

            response = Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            response = chain.proceed(chain.request())
        }

        return response
    }

    companion object {
        // FAKE RESPONSES.
        private val TEACHER_ID_1 = "{\"id\":1,\"age\":28,\"name\":\"Victor Apoyan\"}"
        private val TEACHER_ID_2 = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}"
    }
}
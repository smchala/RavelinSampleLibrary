package com.otssso.samimchala.ravelinlibrary.api

import com.google.gson.Gson
import com.otssso.samimchala.ravelinlibrary.TrampolineSchedulerProvider
import junit.framework.Assert.assertEquals
import org.junit.Test

class RavelinApiTest {

    val blob:String = "{\"customer\":{\"customerId\":\"smchala@hotmail.com\",\"email\":\"smchala@hotmail.com\",\"name\":\"Sami Mchala\"},\"device\":{\"deviceId\":\"8D73C302D2389FDE27F3A259267F8F0BCF66F69C\",\"ipAddress\":\"192.168.232.2\",\"location\":{\"latitude\":37.421998333333335,\"longitude\":-122.08400000000002},\"model\":\"Android SDK built for x86\",\"os\":\"4.4.124+\",\"userAgent\":\"Mozilla/5.0 (Linux; Android 9; Android SDK built for x86 Build/PPP4.180612.007; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36\"},\"timestamp\":1557850800366}"
    @Test
    fun`tst`(){
        val customerApi = RavelinApi.getRavelinClient()
        val json = Gson().toJson(blob)
        val t = customerApi.sendBlob(json)
            .observeOn(TrampolineSchedulerProvider().io())
            .subscribeOn(TrampolineSchedulerProvider().ui())
            .blockingGet().code()

        assertEquals(400, t)
    }
}
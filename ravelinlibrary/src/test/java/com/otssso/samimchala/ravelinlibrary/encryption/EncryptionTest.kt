package com.otssso.samimchala.ravelinlibrary.encryption

import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Test

class EncryptionTest {

    @Test
    fun `checking we get a hashed value after encryption`() {
        val e = Encryption()

        val key = "8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7"
        val data =
            "{\"customer\":{\"customerId\":\"smchala@hotmail.com\",\"email\":\"smchala@hotmail.com\",\"name\":\"Sami Mchala\"},\"device\":{\"deviceId\":\"\",\"ipAddress\":\"192.168.232.2\",\"location\":{\"latitude\":37.421998333333335,\"longitude\":-122.08400000000002},\"model\":\"Android SDK built for x86\",\"os\":\"4.4.124+\",\"userAgent\":\"Mozilla/5.0 (Linux; Android 9; Android SDK built for x86 Build/PSR1.180720.012; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36\"},\"timestamp\":1557942466925}\n"
        val encAndGasshed = e.encryptAndHash(key, data.toByteArray())

        assertNotNull(encAndGasshed)
    }

    @Test
    fun `checking the encrypted and hashed value is 40 characters long`() {

        val e = Encryption()

        val key = "8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7"
        val data =
            "{\"customer\":{\"customerId\":\"smchala@hotmail.com\",\"email\":\"smchala@hotmail.com\",\"name\":\"Sami Mchala\"},\"device\":{\"deviceId\":\"\",\"ipAddress\":\"192.168.232.2\",\"location\":{\"latitude\":37.421998333333335,\"longitude\":-122.08400000000002},\"model\":\"Android SDK built for x86\",\"os\":\"4.4.124+\",\"userAgent\":\"Mozilla/5.0 (Linux; Android 9; Android SDK built for x86 Build/PSR1.180720.012; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36\"},\"timestamp\":1557942466925}\n"
        val encAndGasshed = e.encryptAndHash(key, data.toByteArray())

        Assert.assertEquals(40, encAndGasshed.length)
    }
}
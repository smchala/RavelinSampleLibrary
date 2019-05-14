package com.otssso.samimchala.ravelinlibrary.encryption

import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class EncryptionTest {

    //[123,34,99,117,115,116,111,109,101,114,34,58,123,34,99,117,115,116,111,109,101,114,73,100,34,58,34,115,109,99,104,97,108,97,64,104,111,116,109,97,105,108,46,99,111,109,34,44,34,101,109,97,105,108,34,58,34,115,109,99,104,97,108,97,64,104,111,116,109,97,105,108,46,99,111,109,34,44,34,110,97,109,101,34,58,34,83,97,109,105,32,77,99,104,97,108,97,34,125,44,34,100,101,118,105,99,101,34,58,123,34,100,101,118,105,99,101,73,100,34,58,34,34,44,34,105,112,65,100,100,114,101,115,115,34,58,34,49,57,50,46,49,54,56,46,50,51,50,46,50,34,44,34,108,111,99,97,116,105,111,110,34,58,123,34,108,97,116,105,116,117,100,101,34,58,51,55,46,52,50,49,57,57,56,51,51,51,51,51,51,51,51,53,44,34,108,111,110,103,105,116,117,100,101,34,58,45,49,50,50,46,48,56,52,48,48,48,48,48,48,48,48,48,48,50,125,44,34,109,111,100,101,108,34,58,34,65,110,100,114,111,105,100,32,83,68,75,32,98,117,105,108,116,32,102,111,114,32,120,56,54,34,44,34,111,115,34,58,34,52,46,52,46,49,50,52,43,34,44,34,117,115,101,114,65,103,101,110,116,34,58,34,77,111,122,105,108,108,97,47,53,46,48,32,40,76,105,110,117,120,59,32,65,110,100,114,111,105,100,32,57,59,32,65,110,100,114,111,105,100,32,83,68,75,32,98,117,105,108,116,32,102,111,114,32,120,56,54,32,66,117,105,108,100,47,80,83,82,49,46,49,56,48,55,50,48,46,48,49,50,59,32,119,118,41,32,65,112,112,108,101,87,101,98,75,105,116,47, 53, 51, 55, 46, 51, 54, 32, 40, 75, 72, 84, 77, 76, 44, 32, 108, 105, 107, 101, 32, 71, 101, 99, 107, 111, 41, 32, 86, 101, 114, 115, 105, 111, 110, 47, 52, 46, 48, 32, 67, 104, 114, 111, 109, 101, 47, 55, 52, 46, 48, 46, 51, 55, 50, 57, 46, 49, 51,54,32,77,111,98,105,108,101,32,83,97,102,97,114,105,47,53,51,55,46,51,54,34,125,44,34,116,105,109,101,115,116,97,109,112,34,58,49,53,53,55,56,53,56,54,55,52,56,51,50,125]
    @Ignore
    @Test
    fun `testing ecryption`(){
        val e = Encryption()
        val k = setKey("8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7".toByteArray())


        val enc = e.encryptAndHash(String(k), "{\"customer\":{\"customerId\":\"smchala@hotmail.com\",\"email\":\"smchala@hotmail.com\",\"name\":\"Sami Mchala\"},\"device\":{\"deviceId\":\"\",\"ipAddress\":\"192.168.232.2\",\"location\":{\"latitude\":37.421998333333335,\"longitude\":-122.08400000000002},\"model\":\"Android SDK built for x86\",\"os\":\"4.4.124+\",\"userAgent\":\"Mozilla/5.0 (Linux; Android 9; Android SDK built for x86 Build/PSR1.180720.012; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36\"},\"timestamp\":1557858457801}".toByteArray())


        assertEquals("{\"customer\":{\"customerId\":\"smchala@hotmail.com\",\"email\":\"smchala@hotmail.com\",\"name\":\"Sami Mchala\"},\"device\":{\"deviceId\":\"\",\"ipAddress\":\"192.168.232.2\",\"location\":{\"latitude\":37.421998333333335,\"longitude\":-122.08400000000002},\"model\":\"Android SDK built for x86\",\"os\":\"4.4.124+\",\"userAgent\":\"Mozilla/5.0 (Linux; Android 9; Android SDK built for x86 Build/PSR1.180720.012; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36\"},\"timestamp\":1557858457801}".toByteArray(), decrypt(k, enc.toByteArray()))
        assertEquals("D164C3556BAEADE854BEC8F1487ACE3F4CDBF392", enc.toByteArray())
//        assertEquals("34D8F66047E5B1D6A5BD8FA11E0186818A645DBE", e.encryptAndHash("8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7", "1234567890".toByteArray()))

    }


    @Throws(Exception::class)
    protected fun decrypt(raw: ByteArray, encrypted: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }

    private fun setKey(keyStart: ByteArray): ByteArray {
        val kgen = KeyGenerator.getInstance("AES")
        val sr = SecureRandom.getInstance("SHA1PRNG")
        sr.setSeed(keyStart)
        kgen.init(128, sr) // 192 and 256 bits may not be available
        val skey = kgen.generateKey()
        val key = skey.getEncoded()
        return key
    }
}
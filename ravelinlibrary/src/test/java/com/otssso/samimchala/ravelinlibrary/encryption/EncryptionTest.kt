package com.otssso.samimchala.ravelinlibrary.encryption

import org.junit.Test
import org.junit.Assert.assertEquals
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class EncryptionTest {

    @Test
    fun `testing ecryption`(){

        val e = Encryption()
        val k = setKey("8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7".toByteArray())
        assertEquals("1234567890".toByteArray(), decrypt(k, "".toByteArray()))
        assertEquals("34D8F66047E5B1D6A5BD8FA11E0186818A645DBE", e.encryptAndHash("8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7", "1234567890".toByteArray()))

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
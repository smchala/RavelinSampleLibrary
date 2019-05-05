package com.otssso.samimchala.ravelinlibrary.encryption

import android.util.Log
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class Encryption{

    @Throws(Exception::class)
    private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        return cipher.doFinal(clear)
    }

    @Throws(Exception::class)
    private fun decrypt(raw: ByteArray, encrypted: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }

    fun test(key1:String, data:ByteArray){
        Log.d("sm", "--------=-0=-0=-0-0  key1 ${key1}")
        val keyStart = key1.toByteArray()
        Log.d("sm", "--------=-0=-0=-0-0  keyStart ${String(keyStart)}")

        val kgen = KeyGenerator.getInstance("AES")
        val sr = SecureRandom.getInstance("SHA1PRNG")
        sr.setSeed(keyStart)
        kgen.init(128, sr) // 192 and 256 bits may not be available
        val skey = kgen.generateKey()
        val key = skey.getEncoded()

// encrypt
        val encryptedData = encrypt(key, data)
//        Log.d("sm", "encryptedData =-0=-0=-0  ${String(encryptedData)}")

        val t = HashUtils.sha1(String(encryptedData))
        Log.d("sm", "hash =-0=-0=-0  ${t}")

// decrypt
        val decryptedData = decrypt(key, encryptedData)
        Log.d("sm", "decryptedData =-0=-0=-0  ${ String(decryptedData)}")

    }
}
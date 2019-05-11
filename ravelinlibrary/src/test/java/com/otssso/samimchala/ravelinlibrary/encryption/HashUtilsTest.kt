package com.otssso.samimchala.ravelinlibrary.encryption

import org.junit.Assert
import org.junit.Test

class HashUtilsTest{
    @Test
    fun `testing hash function`(){
        val e = HashUtils
        Assert.assertEquals("01B307ACBA4F54F55AAFC33BB06BBBF6CA803E9A", e.sha1("1234567890"))
    }
}
package com.otssso.samimchala.ravelinlibrary.encryption

import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Test
import java.util.regex.Pattern

class HashUtilsTest {
    @Test
    fun `testing hash function`() {
        val e = HashUtils
        Assert.assertEquals("01B307ACBA4F54F55AAFC33BB06BBBF6CA803E9A", e.sha1("1234567890"))
    }

    @Test
    fun `checking the hashed value is 40 charachters long`() {

        val e = HashUtils
        Assert.assertEquals(40, e.sha1("1234567890").length)
        assertTrue(true)
    }

//    val hexadecimalPattern = Pattern.compile("\\p{XDigit}+")
    val hexadecimalPattern = Pattern.compile("^[0-9a-fA-F]+$")

    @Test
    fun `checking the hashed value is a hexadecimal value`() {
        val e = HashUtils
        val hex = e.sha1("1234567890")
        assertTrue(hexadecimalPattern.matcher(hex).matches())
    }
}
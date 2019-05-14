package com.otssso.samimchala.ravelinlibrary.data

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CustomerTest {

    lateinit var sut: Customer

    @Before
    fun setUp() {
        sut = Customer()
    }

    @Test
    fun `check customer object has default values`() {
        Assert.assertEquals("default", sut.customerId)
        Assert.assertEquals("email@email.com", sut.email)
        Assert.assertEquals("default", sut.name)
    }

    @Test
    fun `check customer object has all fields`() {
        sut = Customer(
            "customerId",
            "email@email.com",
            "name"
        )
        Assert.assertEquals("name", sut.name)
        Assert.assertEquals("customerId", sut.customerId)
        Assert.assertEquals("email@email.com", sut.email)
    }

    @Test
    fun `check customer name does only contains aplhabetical letters, no special characters`() {
        sut = Customer(name = "name=-0")
        Assert.assertEquals("default", sut.name)
    }

    @Test
    fun `check customer name does only contains aplhabetical letters, no numbers`() {
        sut = Customer(name = "1234name")
        Assert.assertEquals("default", sut.name)
    }

    @Test
    fun `check customer email is a valid one`() {
        sut = Customer(email = "1234name")
        Assert.assertEquals("email@email.com", sut.email)
    }

    @Test
    fun `check if string is a made of alphabet only`() {
        val (letters, notLetters) = "name".toCharArray().partition { it.isLetter() }
        assert(notLetters.size == 0)
        assert(letters.size == 4)
    }

    @Test
    fun `check if string with numbers is rejected`() {
        val (letters, notLetters) = "name1234".toCharArray().partition { it.isLetter() }
        assert(notLetters.size == 4)
        assert(letters.size == 4)
    }

    @Test
    fun `check if string with special characters is rejected`() {
        val (letters, notLetters) = "name=-".toCharArray().partition { it.isLetter() }
        assert(notLetters.size == 2)
        assert(letters.size == 4)
    }
}
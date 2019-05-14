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
        Assert.assertEquals("default", sut.email)
        Assert.assertEquals("default", sut.name)
    }

    @Test
    fun `check customer object has all fields`() {
        sut = Customer(
            "customerId",
            "email",
            "name"
        )
        Assert.assertEquals("customerId", sut.customerId)
        Assert.assertEquals("email@email.com", sut.email)
        Assert.assertEquals("name", sut.name)
    }

    @Test
    fun `check customer name does only contains aplhabetical letters, no special characters`(){
        sut = Customer(name = "name=-0")
        Assert.assertEquals("default", sut.name)
    }

    @Test
    fun `check customer name does only contains aplhabetical letters, no numbers`(){
        sut = Customer(name = "1234name")
        Assert.assertEquals("default", sut.name)
    }

    @Test
    fun `check customer email is a valid one`(){
        sut = Customer(email = "1234name")
        Assert.assertEquals("email@email.com", sut.email)
    }
}
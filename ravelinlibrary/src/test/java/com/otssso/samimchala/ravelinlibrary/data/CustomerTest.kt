package com.otssso.samimchala.ravelinlibrary.data

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CustomerTest {

    lateinit var sut: Customer

    @Before
    fun setUp() {
        sut = Customer(
            "customerId",
            "email",
            "name"
        )
    }

    @Test
    fun `check customer object has all fields`() {
        Assert.assertEquals("customerId", sut.customerId)
        Assert.assertEquals("email", sut.email)
        Assert.assertEquals("name", sut.name)
    }
}
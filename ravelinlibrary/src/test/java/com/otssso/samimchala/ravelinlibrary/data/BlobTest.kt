package com.otssso.samimchala.ravelinlibrary.data

import android.content.Context
import android.location.LocationManager
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BlobTest {

    lateinit var sut: Blob
    @Mock
    lateinit var customer: Customer
    @Mock
    lateinit var device: Device
    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockLocationManager: LocationManager

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(mockContext.getSystemService(Context.LOCATION_SERVICE))
            .thenReturn(mockLocationManager)

        customer = Customer()
        device = Device(
            mockContext,
            "1.1.1.1",
            "userAgent",
            "Google",
            "4.4.4",
            "product",
            Location(mockContext)
        )
        sut = Blob(customer, device, 1234)
    }

    @Test
    fun `check blob has a time stamp`() {
        Assert.assertEquals(1234, sut.timestamp)
    }

    @Test
    fun `check blob has a device object`() {
        assertEquals("product", sut.device.deviceId)
        assertEquals("1.1.1.1", sut.device.ipAddress)
        assertEquals("userAgent", sut.device.userAgent)
        assertEquals("4.4.4", sut.device.os)
        assertEquals(0.0, sut.device.location.latitude)
        assertEquals(0.0, sut.device.location.latitude)
    }

    @Test
    fun `check blob has a customer object`() {
        assertEquals("default", sut.customer.customerId)
        assertEquals("default", sut.customer.customerId)
        assertEquals("default", sut.customer.customerId)
    }
}
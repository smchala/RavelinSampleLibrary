package com.otssso.samimchala.ravelinlibrary.data

import android.content.Context
import android.location.LocationManager
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
        Assert.assertEquals(1234, sut.timeStamp)
    }
}
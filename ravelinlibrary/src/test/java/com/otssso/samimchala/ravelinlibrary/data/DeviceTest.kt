package com.otssso.samimchala.ravelinlibrary.data

import android.content.Context
import android.location.LocationManager
import android.location.LocationProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeviceTest {

    private lateinit var sut: Device
    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockLocationManager: LocationManager
    @Mock
    lateinit var newAndroidLocation: android.location.Location
    @Mock
    lateinit var mockLocation: Location

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        newAndroidLocation = mock(android.location.Location::class.java)

        `when`(mockContext.getSystemService(Context.LOCATION_SERVICE))
            .thenReturn(mockLocationManager)

        mockLocation = Location(mockContext)

        sut = Device(
            mockContext,
            "1.1.1.1",
            "userAgent",
            "Google",
            "4.4.4",
            "deviceId",
            mockLocation
        )
    }

    @Test
    fun `checking the device class has all the mandatory fields`() {
        assertEquals("deviceId", sut.deviceId)
        assertEquals("1.1.1.1", sut.ipAddress)
        assertEquals("userAgent", sut.userAgent)
        assertEquals("4.4.4", sut.os)
        assertEquals("deviceId", sut.deviceId)
    }
}
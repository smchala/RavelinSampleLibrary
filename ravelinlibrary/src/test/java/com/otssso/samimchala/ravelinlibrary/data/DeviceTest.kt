package com.otssso.samimchala.ravelinlibrary.data


import android.content.Context
import android.location.LocationManager
import android.location.LocationProvider
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
class DeviceTest{

    private lateinit var sut: Device
    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockLocationManager: LocationManager
    @Mock
    lateinit var newAndroidLocation: android.location.Location

    @Before
    fun setUp(){

        MockitoAnnotations.initMocks(this)
        newAndroidLocation = mock(android.location.Location::class.java)

        `when`(mockContext.getSystemService(Context.LOCATION_SERVICE))
            .thenReturn(mockLocationManager)

        sut = Device(
            mockContext,
            "0123456789",
            "1.1.1.1",
            "userAgent",
            "Google",
            "4.4.4",
            "product",
            "deviceId",
            "finger print",
            "user",
            Location(mockContext)
            )
    }

    @Test
    fun `checking the device class has all the mandatory fields`(){
        assertEquals( "0123456789", sut.phoneNumber)
        assertEquals("deviceId", sut.deviceId)
        assertEquals( "1.1.1.1", sut.ipAddress)
        assertEquals( "userAgent", sut.userAgent)
        assertEquals( "Google", sut.phoneModel)
        assertEquals( "4.4.4", sut.os)
        assertEquals( "product", sut.product)
        assertEquals("deviceId", sut.deviceId)
        assertEquals( "finger print", sut.fingerPrint)
        assertEquals( "user", sut.user)
    }

    private fun getMockLocation() {
        mockLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER)
        mockLocationManager.addTestProvider(
            LocationManager.GPS_PROVIDER,
            "requiresNetwork" === "",
            "requiresSatellite" === "",
            "requiresCell" === "",
            "hasMonetaryCost" === "",
            "supportsAltitude" === "",
            "supportsSpeed" === "",
            "supportsBearing" === "",
            android.location.Criteria.POWER_LOW,
            android.location.Criteria.ACCURACY_FINE
        )

        newAndroidLocation.latitude = 37.421998333333335
        newAndroidLocation.longitude = -122.08400000000002

        newAndroidLocation.accuracy = 500F

        mockLocationManager.setTestProviderEnabled(
            LocationManager.GPS_PROVIDER,
            true
        )

        mockLocationManager.setTestProviderStatus(
            LocationManager.GPS_PROVIDER,
            LocationProvider.AVAILABLE,
            null,
            System.currentTimeMillis()
        )

        mockLocationManager.setTestProviderLocation(
            LocationManager.GPS_PROVIDER,
            newAndroidLocation
        )
    }
}
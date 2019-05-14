package com.otssso.samimchala.ravelinlibrary.data

import android.content.Context
import android.location.LocationManager
import android.location.LocationProvider
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class LocationTest{

    lateinit var sut: Location
    @Mock
    private lateinit var newLocation: android.location.Location
    @Mock
    lateinit var mockContext:Context
    @Mock
    lateinit var mockLocationManager: LocationManager

    @Before
    fun setUp(){

        mockContext = mock(Context::class.java)
        sut = Location(mockContext)

        mockLocationManager = mock(LocationManager::class.java)
        newLocation = mock(android.location.Location::class.java)

        mockLocationManager.addTestProvider(LocationManager.GPS_PROVIDER,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            android.location.Criteria.POWER_LOW,
            android.location.Criteria.ACCURACY_FINE)

//        newLocation = android.location.Location(LocationManager.GPS_PROVIDER)

        mockLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)

        mockLocationManager.setTestProviderStatus(
            LocationManager.GPS_PROVIDER,
            LocationProvider.AVAILABLE,
            null, System.currentTimeMillis()
        )

        mockLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation)
        sut.locationManager = mockLocationManager


        newLocation.latitude = 1.0
        newLocation.longitude = 1.0
        sut.latitude = 0.0
        sut.longitude = 0.0
    }

    @Test
    fun `testing we getting default location, longitude and latitude`(){
        assertEquals(0.0, sut.latitude)
        assertEquals(0.0, sut.longitude)
    }

    @Test
    fun `checking coordinates have a default value`() {
        assertEquals(sut.coordinates().value,Pair(0.0,0.0))
    }

    @Test
    fun `checking coordinates are returned when longLat is set by the locationListener callback`() {
        sut.longLat.onNext(Pair(10.0,10.0))
        assertEquals(sut.coordinates().value,Pair(10.0,10.0))
    }

    @Test
    fun `checking when new coordinates are set, stopListener is invoked`() {
        sut.onLocationChanged(newLocation)
        verify(mockLocationManager, atLeastOnce()).removeUpdates(sut)
    }
}

package com.otssso.samimchala.ravelinlibrary.data

import android.content.Context
import android.location.LocationManager
import android.location.LocationProvider
import android.util.Log
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks


class LocationTest{

    @Mock
    lateinit var sut: Location
    @Mock
    lateinit var mockContext:Context

    @Before
    fun setUp(){

        mockContext = mock(Context::class.java)
        sut = Location(mockContext)
        initMocks(sut)

        sut.locationManager?.addTestProvider(LocationManager.GPS_PROVIDER,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            android.location.Criteria.POWER_LOW,
            android.location.Criteria.ACCURACY_FINE)

        val newLocation = android.location.Location(LocationManager.GPS_PROVIDER)
        sut.locationManager?.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)

        sut.locationManager?.setTestProviderStatus(
            LocationManager.GPS_PROVIDER,
            LocationProvider.AVAILABLE,
            null, System.currentTimeMillis()
        )
        sut.locationManager?.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation)

        sut.latitude = 1.0
        sut.longitude = 1.0
    }

    @Test
    fun `checking we can get longitude and latitude`(){

        assertEquals(1.0, sut.latitude)
        assertEquals(1.0, sut.longitude)
    }


    @Test
    fun `verify that stopListener is invoked once we receive a location`(){
//        Log.d("sm", "=-0=-0=-0=-0 providers:  ${sut.locationManager?.getProviders(true)?.size}")

        assertEquals(1.0, sut.latitude)

        verify(sut.locationManager, atLeastOnce())?.removeUpdates(sut)
//        Log.d("sm", "=-0=-0=-0=-0 providers:  ${sut.locationManager?.getProviders(true)?.size}")


//        sut.locationManager?.getLastKnownLocation()?.latitude


    }


}

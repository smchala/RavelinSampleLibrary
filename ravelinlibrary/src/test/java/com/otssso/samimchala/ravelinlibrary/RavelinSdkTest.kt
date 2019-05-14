package com.otssso.samimchala.ravelinlibrary

import android.content.Context
import android.location.LocationManager
import com.google.gson.Gson
import com.otssso.samimchala.ravelinlibrary.data.Blob
import com.otssso.samimchala.ravelinlibrary.data.Customer
import com.otssso.samimchala.ravelinlibrary.data.Device
import com.otssso.samimchala.ravelinlibrary.data.Location
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RavelinSdkTest {

    lateinit var sut: RavelinSdk
    @Mock
    private var mockContext: Context = mock(Context::class.java)
    @Mock
    private lateinit var mockDevice: Device
    @Mock
    private lateinit var mockCustomer: Customer
    @Mock
    private lateinit var mockLocationManager: LocationManager
    @Mock
    lateinit var mockLocation: Location

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(mockContext.getSystemService(Context.LOCATION_SERVICE))
            .thenReturn(mockLocationManager)
        mockLocation = Location(mockContext)
        mockLocation.longitude = 100.0
        mockLocation.latitude = 100.0

        mockDevice = Device(
            mockContext,
            "1.1.1.1",
            "userAgent",
            "Google",
            "4.4.4",
            "product",
            mockLocation
        )

        mockCustomer = Customer(
            "customerId",
            "email",
            "name"
        )

        sut = RavelinSdk.Builder(mockContext, mockDevice, mockCustomer, TrampolineSchedulerProvider())
            .setEmail("email")
            .setName("name")
            .setSecretKey("secret")
            .create()
    }

    @After
    fun cleanUp() {
        sut.destroy()
    }

    @Test
    fun `checking SDK is initialised`() {
        Assert.assertEquals("email", sut.builder.customer.email)
        Assert.assertEquals("name", sut.builder.customer.name)
        Assert.assertEquals("secret", sut.builder.key)
    }

    @Test
    fun `checking json blob has timestamp`() {

        sut.getBlobJSON()?.let { observer ->
            var result = ""
            observer.subscribeOn(TrampolineSchedulerProvider().io())
                .observeOn(TrampolineSchedulerProvider().ui())
                .subscribe {
                    result = it
                }

            val json = Gson().fromJson<Blob>(result, Blob::class.java)

            assertNotNull(json.timestamp)
        }
    }

    @Test
    fun `checking json blob has a device object`() {

        sut.getBlobJSON()?.let { observer ->
            var result = ""
            observer.subscribeOn(TrampolineSchedulerProvider().io())
                .observeOn(TrampolineSchedulerProvider().ui())
                .subscribe {
                    result = it
                }

            val json = Gson().fromJson<Blob>(result, Blob::class.java)

            assertEquals(40, json.device.deviceId.length)
            assertEquals("4.4.4", json.device.os)
            assertEquals("userAgent", json.device.userAgent)
            assertEquals("Google", json.device.model)
            assertEquals(100.0, json.device.location.latitude)
            assertEquals(100.0, json.device.location.latitude)
        }
    }

    @Test
    fun `checking json blob has a customer object`() {
        sut.getBlobJSON()?.let { observer ->
            var result = ""
            observer.subscribeOn(TrampolineSchedulerProvider().io())
                .observeOn(TrampolineSchedulerProvider().ui())
                .subscribe {
                    result = it
                }

            val json = Gson().fromJson<Blob>(result, Blob::class.java)

            assertEquals("email", json.customer.customerId)
            assertEquals("name", json.customer.name)
            assertEquals("email", json.customer.email)
        }
    }
}



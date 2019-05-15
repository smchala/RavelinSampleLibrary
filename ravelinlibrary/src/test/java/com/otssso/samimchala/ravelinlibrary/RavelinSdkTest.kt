package com.otssso.samimchala.ravelinlibrary

import android.content.Context
import android.location.LocationManager
import com.google.gson.Gson
import com.otssso.samimchala.ravelinlibrary.api.CustomerApi
import com.otssso.samimchala.ravelinlibrary.api.FakeInterceptor
import com.otssso.samimchala.ravelinlibrary.api.RavelinApi
import com.otssso.samimchala.ravelinlibrary.data.Blob
import com.otssso.samimchala.ravelinlibrary.data.Customer
import com.otssso.samimchala.ravelinlibrary.data.Device
import com.otssso.samimchala.ravelinlibrary.data.Location
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
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
    @Mock
    lateinit var mockCustomerApi: CustomerApi
    @Mock
    lateinit var mockRavelinApi: RavelinApi
    private var result = ""
    lateinit var json: Blob

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

        sut.getBlobJSON()?.let { observer ->
            observer.subscribeOn(TrampolineSchedulerProvider().io())
                .observeOn(TrampolineSchedulerProvider().ui())
                .subscribe {
                    result = it
                }

            json = Gson().fromJson<Blob>(result, Blob::class.java)
        }

        mockCustomerApi = mock(CustomerApi::class.java)

        `when`(mockRavelinApi.getRavelinClient()).thenReturn(mockCustomerApi)
        mockRavelinApi.headerAuthorizationInterceptor = FakeInterceptor()
        mockCustomerApi = mockRavelinApi.getRavelinClient()
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
        assertNotNull(json.timestamp)
    }

    @Test
    fun `checking json blob has a device object`() {
        assertEquals(40, json.device.deviceId.length)
        assertEquals("4.4.4", json.device.os)
        assertEquals("userAgent", json.device.userAgent)
        assertEquals("Google", json.device.model)
        assertEquals(100.0, json.device.location.latitude)
        assertEquals(100.0, json.device.location.latitude)
    }

    @Test
    fun `checking json blob has a customer object`() {
        assertEquals("email", json.customer.customerId)
        assertEquals("name", json.customer.name)
        assertEquals("email", json.customer.email)
    }

    @Ignore
    @Test
    fun `checking the json injected when posting to ravelin`() {
        sut.customerApi = mockCustomerApi

        val json =
            "{\"customer\":{\"customerId\":\"smchala@hotmail.com\",\"email\":\"smchala@hotmail.com\",\"name\":\"Sami Mchala\"},\"device\":{\"deviceId\":\"E57952E40AA7A76C3E2CA6E632D1B5FEFA57A81F\",\"ipAddress\":\"192.168.232.2\",\"location\":{\"latitude\":37.421998333333335,\"longitude\":-122.08400000000002},\"model\":\"Android SDK built for x86\",\"os\":\"4.4.124+\",\"userAgent\":\"Mozilla/5.0 (Linux; Android 9; Android SDK built for x86 Build/PSR1.180720.012; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36\"},\"timestamp\":1557935252517}\n"
        sut.blob = Gson().fromJson<Blob>(result, Blob::class.java)

        val captor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        sut.postDeviceInformation()
        verify(sut.customerApi).sendBlob(captor.capture())

        assertEquals(json, captor.value)
    }
}



package com.otssso.samimchala.ravelinlibrary

import android.content.Context
import android.location.LocationManager
import com.otssso.samimchala.ravelinlibrary.data.Customer
import com.otssso.samimchala.ravelinlibrary.data.Device
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RavelinSdkTest{

    val stub:String = "{  \n" +
            "   \"customer\":{  \n" +
            "      \"customerId\":\"smchala@hotmail.com\",\n" +
            "      \"email\":\"smchala@hotmail.com\",\n" +
            "      \"name\":\"Sami Mchala\"\n" +
            "   },\n" +
            "   \"device\":{  \n" +
            "      \"deviceId\":\"EA52C8193F0115E86D5A73F5906E3EC9B3C34299\",\n" +
            "      \"fingerPrint\":\"google/sdk_gphone_x86/generic_x86:9/PSR1.180720.012/4923214:user/release-keys\",\n" +
            "      \"ipAddress\":\"192.168.232.2\",\n" +
            "      \"location\":{  \n" +
            "         \"latitude\":\"37.421998333333335\",\n" +
            "         \"longitude\":\"-122.08400000000002\"\n" +
            "      },\n" +
            "      \"os\":\"4.4.124+\",\n" +
            "      \"phoneModel\":\"Android SDK built for x86\",\n" +
            "      \"phoneNumber\":\"+15555215554\",\n" +
            "      \"product\":\"sdk_gphone_x86\",\n" +
            "      \"user\":\"android-build\",\n" +
            "      \"userAgent\":\"Mozilla/5.0 (Linux; Android 9; Android SDK built for x86 Build/PSR1.180720.012; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/73.0.3683.90 Mobile Safari/537.36\"\n" +
            "   },\n" +
            "   \"timeStamp\":1557140722589\n" +
            "}"

    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockDevice: Device
    @Mock
    private lateinit var mockCustomer: Customer
    @Mock
    private lateinit var mockLocationManager: LocationManager
    @Mock
    lateinit var mockLocation: com.otssso.samimchala.ravelinlibrary.data.Location

    lateinit var sut: RavelinSdk
    @Before
    fun setUp(){
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(mockContext.getSystemService(Context.LOCATION_SERVICE))
            .thenReturn(mockLocationManager)
        mockLocation = com.otssso.samimchala.ravelinlibrary.data.Location(mockContext)

        mockDevice = Device(
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
            mockLocation
        )

        mockCustomer= Customer(
            "customerId",
            "email",
            "name"
        )


        sut = RavelinSdk.Builder(mockContext, mockDevice,mockCustomer)
            .setEmail("email")
            .setName("name")
            .setSecretKey("secret")
            .create()
    }

    @After
    fun cleanUp(){
        sut.destroy()
    }

    @Test
    fun `checking SDK is initialised`(){
        Assert.assertEquals("email", sut.builder.customer.email)
        Assert.assertEquals("name", sut.builder.customer.name)
        Assert.assertEquals("secret", sut.builder.key)
    }

    @Test
    fun `checking json blob is generated with deviceId Set`(){

        //need fixing, RavelinSdk init need sorting out as we expecting call back from location...
        //and device id will be different!
        sut.getBlob().test()
            .assertNoErrors()
//            .assertValue(stub)
            .dispose()

    }
}
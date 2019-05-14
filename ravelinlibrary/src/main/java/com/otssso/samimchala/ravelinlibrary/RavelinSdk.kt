package com.otssso.samimchala.ravelinlibrary

import android.content.Context
import com.google.gson.Gson
import com.otssso.samimchala.ravelinlibrary.api.RavelinApi
import com.otssso.samimchala.ravelinlibrary.data.Blob
import com.otssso.samimchala.ravelinlibrary.data.Customer
import com.otssso.samimchala.ravelinlibrary.data.Device
import com.otssso.samimchala.ravelinlibrary.encryption.Encryption
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import retrofit2.Response

class RavelinSdk(
    val builder: Builder,
    private val schedulerProvider: BaseSchedulerProvider,//experimenting
    private val encryption: Encryption = Encryption()
) {

    var blobJson: PublishSubject<String> = PublishSubject.create()
    private lateinit var blob: Blob

    private fun convertToJson(blob: Blob): String = Gson().toJson(blob)

    fun getBlobJSON(): Observable<String> {
        //get the coordinates
        //then create the original json blob without deviceId
        //encrypt it + hash
        //re-create the json including the deviceId
        return builder.device.location.coordinates()
            .compose(convertToJSON())
            .compose(encrypt())
            .compose(convertFromStringToJSON())
    }

    private fun convertFromStringToJSON(): ObservableTransformer<String, String> {
        return ObservableTransformer {
            it.map { encryptedAndHashedJsonBlob ->
                blob.device.deviceId = encryptedAndHashedJsonBlob
                convertToJson(blob)
            }
        }
    }

    private fun encrypt(): ObservableTransformer<String, String> {
        return ObservableTransformer {
            it.map { json ->
                encryption.encryptAndHash(
                    builder.key,
                    json.toByteArray()
                )//key in the clear! try to make it less obvious, obfuscation...
            }
        }
    }

    private fun convertToJSON(): ObservableTransformer<Pair<Double, Double>, String> {
        return ObservableTransformer {
            it.map {
                blob = Blob(builder.customer, builder.device)
                convertToJson(blob)
            }
        }
    }

    fun postDeviceInformation(): Single<Response<String>> {
        val customerApi = RavelinApi.getRavelinClient()
        val json = Gson().toJson(blob)
        return customerApi.sendBlob(json)
    }

    class Builder(
        val context: Context,
        val device: Device = Device(context),
        val customer: Customer = Customer(),
        val schedulerProvider: BaseSchedulerProvider = SchedulerProvider()
    ) {

        private lateinit var name: String
        private lateinit var email: String
        lateinit var key: String

        fun setName(name: String): Builder {
            this.name = name
            return this
        }

        fun setEmail(email: String): Builder {
            this.email = email
            return this
        }

        fun create(): RavelinSdk {
            this.customer.customerId = email
            this.customer.email = email
            this.customer.name = name
            return RavelinSdk(this, schedulerProvider)
        }

        fun setSecretKey(key: String): Builder {
            this.key = key
            return this
        }
    }

    fun destroy() {
        blobJson = PublishSubject.create()
        builder.setName("")
        builder.setEmail("")
        builder.setSecretKey("")
        //...
    }
}

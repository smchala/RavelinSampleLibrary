package com.otssso.samimchala.ravelinlibrary

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.otssso.samimchala.ravelinlibrary.api.RavelinApi
import com.otssso.samimchala.ravelinlibrary.data.Blob
import com.otssso.samimchala.ravelinlibrary.data.Customer
import com.otssso.samimchala.ravelinlibrary.data.Device
import com.otssso.samimchala.ravelinlibrary.encryption.Encryption
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RavelinSdk(
    val builder: Builder,
    private val schedulerProvider: BaseSchedulerProvider,
    val encryption: Encryption = Encryption()
) {

    private var blobString: String = ""
    var blobJson: PublishSubject<String> = PublishSubject.create()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var blob: Blob

    private fun convertToJson(blob: Blob): String = Gson().toJson(blob)

    fun getBlobJSON(): Observable<String> {

//        val coordinates = builder.device.location.coordinates()
//            .subscribeOn(schedulerProvider.io())
//            .map {
//                blob = Blob(builder.customer, builder.device)
//                convertToJson(blob)
//            }
//            .map { json ->
//                encryption.encryptAndHash(
//                    builder.key,
//                    json.toByteArray()
//                )//key in the clear! try to make it less obvious, obfuscation...
//
//            }.map { encryptedAndHashedJsonBlob ->
//                blob.device.deviceId = encryptedAndHashedJsonBlob
//                convertToJson(blob)
//            }
//            .observeOn(schedulerProvider.ui())
//            .subscribe { json ->
//                Log.d("sm", "getBlobJSON =-0=-0=-0=-0-0 ${json}")
//                blobJson.onNext(json)
//                blobString = json
//            }

        //this will wait for the coordinates to show up
        //then create the original json blob with deviceId
        //encrypt it + hash
        //update the original json
        //send it on its way!
        return builder.device.location.coordinates()
            .compose(convertToJSON())
            .compose(encrypt())
            .compose(convertFroStringToJSON())

    }


    private fun convertFroStringToJSON(): ObservableTransformer<String, String> {
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

//    fun getBlob(): String {
//        return blobString
//    }

    fun postDeviceInformation() {
        val customerApi = RavelinApi.getRavelinClient()

        val json = Gson().toJson(blob)
        compositeDisposable.add(customerApi.sendBlob(json)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> Log.d("sm", "SUCCESS ") },
                { error -> Log.e("sm", "ERROR ${error.message} ") }
            ))
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
        compositeDisposable.dispose()
        blobJson = PublishSubject.create()
        builder.setName("")
        builder.setEmail("")
        builder.setSecretKey("")
        //...
    }
}

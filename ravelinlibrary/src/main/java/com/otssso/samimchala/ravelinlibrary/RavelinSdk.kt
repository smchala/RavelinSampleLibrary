package com.otssso.samimchala.ravelinlibrary

import android.content.Context
import android.database.Observable
import android.util.Log
import com.otssso.samimchala.ravelinlibrary.data.Blob
import com.otssso.samimchala.ravelinlibrary.data.Customer
import com.otssso.samimchala.ravelinlibrary.data.Device
import com.google.gson.Gson
import com.otssso.samimchala.ravelinlibrary.encryption.Encryption
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RavelinSdk(private val builder: Builder){

    private var blobJson : PublishSubject<String> = PublishSubject.create()
    private var compositeDisposable: Disposable = CompositeDisposable()

    init {
        compositeDisposable =
            builder.device.location.coordinates().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val blob = Blob(builder.customer, builder.device)
                val json = Gson().toJson(blob)
                Log.d("sm", "=-0=-0=-0=-0=0-   ${blob.timeStamp}")
                Log.d("sm", "=-0=-0=-0=-0=0-   ${json}")

                val e = Encryption()

                e.test(builder.key, json.toByteArray())

                blobJson.onNext(json)
            }
    }

    fun getBlob(): PublishSubject<String> {
        return blobJson
    }

    fun postDeviceInformation(){

    }

    class Builder(context: Context){

        var device: Device = Device(context)
        var customer: Customer = Customer()
        private lateinit var name:String
        private lateinit var email:String
        lateinit var key: String

        fun setName(name:String): Builder {
            this.name = name
            return this
        }

        fun setEmail(email:String): Builder {
            this.email = email
            return this
        }

        fun create():RavelinSdk{
            this.customer.customerId = email
            this.customer.email = email
            this.customer.name = name
            return RavelinSdk(this)
        }

        fun setSecretKey(key: String): Builder {
            this.key = key
            return this
        }
    }

    fun destroy(){
        compositeDisposable.dispose()
        blobJson = PublishSubject.create()
        builder.setName("")
        builder.setEmail("")
        builder.setSecretKey("")
    }
}
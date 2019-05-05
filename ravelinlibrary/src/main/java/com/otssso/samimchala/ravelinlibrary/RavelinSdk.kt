package com.otssso.samimchala.ravelinlibrary

import android.content.Context
import com.otssso.samimchala.ravelinlibrary.data.Customer
import com.otssso.samimchala.ravelinlibrary.data.Device

class RavelinSdk(val builder: Builder){

    fun getDeviceInformation():Device{
        return builder.device
    }

    fun postDeviceInformation(){

    }

    class Builder(context: Context){

        var device: Device = Device(context)
        var customer: Customer = Customer()
        private lateinit var name:String
        private lateinit var email:String

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
    }
}
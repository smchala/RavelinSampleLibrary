package com.otssso.samimchala.ravelinlibrary.data

data class Blob(val customer: Customer, val device: Device, val timeStamp: Long = System.currentTimeMillis())
package com.otssso.samimchala.ravelinlibrary.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.webkit.WebView

@SuppressLint("MissingPermission", "HardwareIds")
class Device(
    context: Context?,
    @Suppress("DEPRECATION") //TODO: find alternative to Formatter.formatIpAddress()
    val ipAddress: String = Formatter.formatIpAddress(
        (context?.applicationContext?.getSystemService(WIFI_SERVICE) as WifiManager?)?.connectionInfo?.ipAddress ?: 0
    ),
    val userAgent: String = WebView(context).settings.userAgentString,
    val model: String = android.os.Build.MODEL,
    val os: String? = System.getProperty("os.version"),
    var deviceId: String = "",
    val location: Location = Location(context)
)
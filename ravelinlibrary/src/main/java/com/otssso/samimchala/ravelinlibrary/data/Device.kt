package com.otssso.samimchala.ravelinlibrary.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.webkit.WebView

@SuppressLint("MissingPermission", "HardwareIds")
class Device(
    context: Context,
    val phoneNumber: String = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number,
    @Suppress("DEPRECATION") //TODO: find alternative to Formatter.formatIpAddress()
    val ipAddress: String = Formatter.formatIpAddress(
        (context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?)?.connectionInfo?.ipAddress ?: 0
    ),
    val userAgent: String = WebView(context).settings.userAgentString,
    val phoneModel: String = android.os.Build.MODEL,
    val os: String = System.getProperty("os.version"),
    val product: String = android.os.Build.PRODUCT,
    val deviceId: String = android.os.Build.ID,
    val fingerPrint: String = android.os.Build.FINGERPRINT,
    val user: String = android.os.Build.USER,
    val location: Location = Location(
        context
    )
)
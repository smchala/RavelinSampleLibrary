package com.otssso.samimchala.ravelinlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class Location @SuppressLint("MissingPermission") constructor(context: Context) :LocationListener{
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val LOCATION_REFRESH_TIME: Long = 5000
    private val LOCATION_REFRESH_DISTANCE: Float = 1.0f
    private var longLat : PublishSubject<Pair<String, String>> = PublishSubject.create()

    init {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE, this)
    }

    override fun onLocationChanged(p0: Location?) {
        p0?.let {location ->
            longLat.onNext (Pair(location.longitude.toString(), location.latitude.toString()))
            stopListener()
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    override fun onProviderEnabled(p0: String?) {}
    override fun onProviderDisabled(p0: String?) {}

    private fun stopListener(){
        locationManager.removeUpdates(this)
    }

    fun coordinates(): Observable<Pair<String, String>> = longLat
}
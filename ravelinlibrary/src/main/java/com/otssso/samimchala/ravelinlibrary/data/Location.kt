package com.otssso.samimchala.ravelinlibrary.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class Location @SuppressLint("MissingPermission") constructor(context: Context?) :LocationListener{
    @Transient private var locationManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    @Transient private val LOCATION_REFRESH_TIME: Long = 5000
    @Transient private val LOCATION_REFRESH_DISTANCE: Float = 1.0f
    @Transient private var longLat : PublishSubject<Pair<String, String>> = PublishSubject.create()
    private var longitude:String = ""
    private var latitude:String = ""
    init {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE, this)
    }

    override fun onLocationChanged(p0: Location?) {
        p0?.let {location ->
            longitude = location.longitude.toString()
            latitude = location.latitude.toString()
            longLat.onNext (Pair(longitude, latitude))
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
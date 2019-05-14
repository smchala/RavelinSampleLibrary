package com.otssso.samimchala.ravelinlibrary.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import io.reactivex.subjects.BehaviorSubject

class Location @SuppressLint("MissingPermission") constructor(@Transient var context: Context?) :LocationListener{
    var longitude:Double = 0.0
    var latitude:Double = 0.0
    @Transient var locationManager: LocationManager? = context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    @Transient private val LOCATION_REFRESH_TIME: Long = 5000
    @Transient private val LOCATION_REFRESH_DISTANCE: Float = 1.0f
    @Transient var longLat : BehaviorSubject<Pair<Double, Double>> = BehaviorSubject.createDefault(Pair(longitude, latitude))

    init {
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE, this)
    }

    override fun onLocationChanged(p0: Location?) {
        p0?.let {location ->
            longitude = location.longitude
            latitude = location.latitude
            longLat.onNext(Pair(longitude, latitude))
            stopListener()
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    override fun onProviderEnabled(p0: String?) {}
    override fun onProviderDisabled(p0: String?) {}

    private fun stopListener(){
        locationManager?.removeUpdates(this)
    }

    fun coordinates(): BehaviorSubject<Pair<Double, Double>> = longLat
}


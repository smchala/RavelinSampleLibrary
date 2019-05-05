package com.otssso.samimchala.ravelinsample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.otssso.samimchala.ravelinlibrary.DeviceInformation
import com.otssso.samimchala.ravelinlibrary.Location
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private var readPhonePermission = Manifest.permission.READ_PHONE_STATE
    var locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private val PERMISSION_REQUEST_CODE = 1
    private lateinit var compositeDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val deviceInformation = DeviceInformation(this)
        val location = Location(this)
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.phoneModel}")
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.ipAddress}")
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.userAgent}")
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.os}")
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.product}")
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.deviceId}")
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.user}")
        Log.d("sm", "=-0=-0=-0  ${deviceInformation.fingerPrint}")

        if(!checkPermission(locationPermission)){
            requestPermission(locationPermission)
        }else{
            compositeDisposable = location.coordinates().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("sm", "LOCATION =-0=-0=-0  ${it.first}")
                    Log.d("sm", "LOCATION =-0=-0=-0  ${it.second}")

                }
        }

        if (!checkPermission(readPhonePermission)) {
            requestPermission(readPhonePermission)
        } else {
            Log.d("sm", "=-0=-0=-0 phone number ${deviceInformation.phoneNumber}")
        }
    }

    private fun requestPermission(permission: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(
                this,
                "Please allow the permission for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        }
        ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied. We can't get phone number.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            result == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}

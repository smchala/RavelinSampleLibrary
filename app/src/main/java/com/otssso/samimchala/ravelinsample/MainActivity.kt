package com.otssso.samimchala.ravelinsample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.otssso.samimchala.ravelinlibrary.RavelinSdk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val INITIAL_PERMS = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val INITIAL_REQUEST = 1
    private val PHONE_STATE_REQUEST = INITIAL_REQUEST + 1
    private val LOCATION_REQUEST = INITIAL_REQUEST + 2
    private var compositeDisposable: Disposable = CompositeDisposable()

    private lateinit var sdk: RavelinSdk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!canAccessLocation() || !canReadPhoneState()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
        } else {
            sdk = RavelinSdk.Builder(this)
                .setEmail("smchala@hotmail.com")
                .setName("Sami Mchala")
                .setSecretKey("8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7")//obviously... :)
                .create()
            getInfo()
        }
    }

    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun canReadPhoneState(): Boolean {
        return hasPermission(Manifest.permission.READ_PHONE_STATE)
    }

    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            INITIAL_REQUEST -> if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_LONG).show()
            }
        }
        getInfo()
    }

    private fun getInfo() {

        compositeDisposable = sdk.getBlob().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("sm", "blob =-0=-0=-0  ${it}")
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        sdk.destroy()
        compositeDisposable.dispose()
    }
}

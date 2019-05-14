package com.otssso.samimchala.ravelinsample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.otssso.samimchala.ravelinlibrary.RavelinSdk
import com.otssso.samimchala.ravelinlibrary.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

//could have created a view model to lift all of the logic from the view, and use data binding
class MainActivity : AppCompatActivity() {

    private val INITIAL_PERMS = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val INITIAL_REQUEST = 1
    private val PHONE_STATE_REQUEST = INITIAL_REQUEST + 1
    private val LOCATION_REQUEST = INITIAL_REQUEST + 2
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var sdk: RavelinSdk
    private var blob: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (!canAccessLocation() || !canReadPhoneState()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
        } else {
            initSdk()
            findViewById<Button>(R.id.device_information).setOnClickListener {

                compositeDisposable.add(sdk.getBlobJSON()?.let { observer ->
                    observer.subscribeOn(SchedulerProvider().io())
                        .observeOn(SchedulerProvider().ui())
                        .subscribe {
                            findViewById<TextView>(R.id.blob).text = it
                            blob = it
                        }
                })
            }

            findViewById<Button>(R.id.send_information).setOnClickListener {

                if (blob.isNotEmpty()) {
                    sdk.postDeviceInformation()
                        .subscribeOn(SchedulerProvider().io())
                        .observeOn(SchedulerProvider().ui())
                        .subscribe(
                            { _ -> Toast.makeText(this, "SUCCESS!", Toast.LENGTH_SHORT).show() },
                            { error -> Toast.makeText(this, "ERROR ${error.message}", Toast.LENGTH_SHORT).show() })
                } else {
                    Toast.makeText(this, "Please get device info!", Toast.LENGTH_SHORT).show()
                }
            }
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
    }

    private fun initSdk() {
        sdk = RavelinSdk.Builder(this, schedulerProvider = SchedulerProvider())
            .setEmail("smchala@hotmail.com")
            .setName("Sami Mchala")
            .setSecretKey("8C182623CD047A0D6593691B2179B98440A91AF01E4BB2BD90D49CC9E9D171E7")//... :)
            .create()
    }

    override fun onDestroy() {
        super.onDestroy()
        sdk.destroy()
        compositeDisposable.dispose()
    }
}

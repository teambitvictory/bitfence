package dev.bitvictory.bitfence

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.navigation.compose.rememberNavController
import dev.bitvictory.bitfence.ui.theme.GeofenceDemoTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.bitvictory.bitfence.geofence.*
import kotlinx.coroutines.flow.StateFlow
import android.location.LocationManager
import androidx.activity.compose.setContent
import dev.bitvictory.bitfence.components.main.MainComponent


@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val buildVersion = android.os.Build.VERSION.SDK_INT

    private val mainViewModel by lazy { viewModels<MainViewModel>().value }

    private lateinit var viewState: StateFlow<MainActivityViewState>

    private var mLocationService: LocationService? = null

    // A PendingIntent for the Broadcast Receiver that handles geofence transitions.
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    // A PendingIntent for the Broadcast Receiver that handles geofence transitions.
    private val locationPendingIntent: PendingIntent by lazy {
        val intent = Intent(this, LocationBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Graph.provide(this)
        viewState = mainViewModel.state
        mainViewModel.initState(buildVersion, this, geofencePendingIntent)

        initializeLocationService()

        setContent {
            GeofenceDemoTheme(activity = this) {
                val navController = rememberNavController()

                MainComponent(navController, mainViewModel)
            }
        }
        createChannel(this)
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.checkGeofencePermission(this)
    }

    /*
     * In all cases, we need to have the location permission.  On Android 10+ (Q) we need to have
     * the background permission as well.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult with code $requestCode")
        mainViewModel.onRequestPermissionResult(requestCode, permissions, grantResults, this)
    }

    private fun initializeLocationService() {
        if (mLocationService == null) {
            val locationManager =
                applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            mLocationService = LocationService(locationManager, locationPendingIntent)
            mLocationService?.addPassiveLocationListener()
        }
    }

}

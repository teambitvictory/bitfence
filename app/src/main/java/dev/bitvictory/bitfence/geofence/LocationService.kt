package dev.bitvictory.bitfence.geofence

import android.app.PendingIntent
import android.util.Log

import android.location.LocationManager
import java.lang.IllegalArgumentException


class LocationService(
    private val locationManager: LocationManager,
    private val locationPendingIntent: PendingIntent,
) {

    private val TAG = "LocationService"

    fun addPassiveLocationListener() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER,
                1000L,
                0F,
                locationPendingIntent
            )
        } catch (ex: SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "network provider does not exist, " + ex.message)
        }
    }

}
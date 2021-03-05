package dev.bitvictory.bitfence.geofence

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat

class GeofencePermissionService(private val buildVersion: Int) {

    /*
     *  Determines whether the app has the appropriate permissions across Android 10+ and all other
     *  Android versions.
     */
    @TargetApi(29)
    fun foregroundAndBackgroundLocationPermissionApproved(context: Context): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermissionApproved =
            if (buildVersion >= Build.VERSION_CODES.Q) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }


    /*
     *  Requests ACCESS_FINE_LOCATION and (on Android 10(Q) ACCESS_BACKGROUND_LOCATION.
     */
    @TargetApi(29)
    fun requestForegroundAndBackgroundLocationPermissions(activity: Activity) {
        if (foregroundAndBackgroundLocationPermissionApproved(activity))
            return
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val resultCode = when (buildVersion) {
            Build.VERSION_CODES.Q -> {
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        Log.d(TAG, "Request foreground only location permission")
        ActivityCompat.requestPermissions(
            activity,
            permissionsArray,
            resultCode
        )
    }

    /*
     *  Requests ACCESS_FINE_LOCATION and (on Android 10(Q) ACCESS_BACKGROUND_LOCATION.
     */
    @TargetApi(30)
    fun requestBackgroundLocationPermissions(activity: Activity) {
        if (foregroundAndBackgroundLocationPermissionApproved(activity) || buildVersion <= Build.VERSION_CODES.Q)
            return
        val permissionsArray = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        val resultCode = REQUEST_BACKGROUND_PERMISSIONS_REQUEST_CODE

        Log.d(TAG, "Request background only location permission")
        ActivityCompat.requestPermissions(
            activity,
            permissionsArray,
            resultCode
        )
    }

}

private const val TAG = "GeofencePermission"

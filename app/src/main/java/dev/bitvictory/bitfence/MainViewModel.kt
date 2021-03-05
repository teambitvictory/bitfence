package dev.bitvictory.bitfence

import android.app.Activity
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import dev.bitvictory.bitfence.data.Location
import dev.bitvictory.bitfence.data.LocationEventDao
import dev.bitvictory.bitfence.data.LocationsDao
import dev.bitvictory.bitfence.geofence.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val locationEventDao: LocationEventDao = Graph.locationEventsDao,
    private val locationsDao: LocationsDao = Graph.locationsDao
) : ViewModel() {

    private val TAG = "MainViewModel"

    private lateinit var geofencePermissionService: GeofencePermissionService
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceService: GeofenceService

    private val permissionGrantedState = MutableStateFlow(false)
    private val permissionErrorState = MutableStateFlow(false)
    private val buildVersionState = MutableStateFlow(-1)

    private val _state = MutableStateFlow(MainActivityViewState())

    val state: StateFlow<MainActivityViewState>
        get() = _state

    init {
        viewModelScope.launch {
            // Combines the latest value from each of the flows, allowing us to generate a
            // view state instance which only contains the latest values.
            combine(
                permissionGrantedState,
                permissionErrorState,
                buildVersionState,
            ) { permissionGranted, permissionError, buildVersion ->
                MainActivityViewState(
                    permissionsGranted = permissionGranted,
                    permissionError = permissionError,
                    buildVersion = buildVersion
                )
            }.catch { throwable ->
                // TODO: emit a UI error here. For now we'll just rethrow
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun initState(buildVersion: Int, activity: Activity, geofencePendingIntent: PendingIntent) {
        geofencingClient = LocationServices.getGeofencingClient(activity)
        geofencePermissionService = GeofencePermissionService(buildVersion)
        geofenceService = GeofenceService(geofencingClient, geofencePendingIntent)
        buildVersionState.value = buildVersion
    }

    fun checkGeofencePermission(activity: Activity) {
        val result =
            geofencePermissionService.foregroundAndBackgroundLocationPermissionApproved(activity)
        Log.d(TAG, "Check Permission Result: $result")
        if (result) {
            permissionGrantedState.value = true
            permissionErrorState.value = false
        } else {
            geofencePermissionService.requestForegroundAndBackgroundLocationPermissions(activity)
        }
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        activity: Activity,
    ) {
        Log.d(TAG,
            "RequestPermissionResult with code: $requestCode and results ${
                grantResults.joinToString(", ")
            } for permissions ${permissions.joinToString(", ")}")
        if (grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED) || (requestCode == REQUEST_BACKGROUND_PERMISSIONS_REQUEST_CODE &&
                    grantResults[BACKGROUND_ONLY_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED)
        ) {
            Log.e(TAG, "Error requesting permissions")
            permissionErrorState.value = true
            permissionGrantedState.value = false
        }
        if (requestCode == REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE && buildVersionState.value > Build.VERSION_CODES.Q) {
            Log.d(TAG, "Request Background for API 30")
            geofencePermissionService.requestBackgroundLocationPermissions(activity)
        }
    }

    fun onNewLocation(location: Location) {
        Log.d(TAG, "onNewLocation: $location")
        viewModelScope.launch {
            val result = geofenceService.addGeofence(location)
            Log.d(TAG, "Adding Location geofence result: $result")
        }
    }

    fun deleteAllLocationEvents() {
        viewModelScope.launch {
            locationEventDao.deleteAll()
        }
    }

    fun refreshAllGeofences() {
        viewModelScope.launch {
            val locations = locationsDao.getLocationList()
            geofenceService.removeGeofences()
            locations.forEach{
                geofenceService.addGeofence(it)
            }
        }
    }

}

data class MainActivityViewState(
    val permissionsGranted: Boolean = false,
    val permissionError: Boolean = false,
    val buildVersion: Int = -1,
)


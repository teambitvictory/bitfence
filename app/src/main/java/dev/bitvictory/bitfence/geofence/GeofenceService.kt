package dev.bitvictory.bitfence.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import dev.bitvictory.bitfence.data.Location
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeofenceService(
    private val geofenceClient: GeofencingClient,
    private val geofencePendingIntent: PendingIntent
) {

    private val TAG = "GeofenceService"

    @SuppressLint("MissingPermission")
    suspend fun addGeofence(location: Location): Boolean { //49.48404448794257, 8.475557936439099
        val geofence = Geofence.Builder()
            .setRequestId(location.id.toString())
            .setCircularRegion(location.latitude,
                location.longitude,
                50F // radius
            )
            .setLoiteringDelay(60000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or GeofencingRequest.INITIAL_TRIGGER_EXIT or GeofencingRequest.INITIAL_TRIGGER_DWELL)
            .addGeofence(geofence)
            .build()

        return suspendCoroutine { cont ->
            geofenceClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                addOnSuccessListener {
                    cont.resume(true)
                }
                addOnFailureListener {
                    Log.e(TAG, "Adding geofence failed with: ${it.message}")
                    cont.resume(false)
                }
            }
        }
    }

    suspend fun removeGeofences(): Boolean = suspendCoroutine { cont ->
        geofenceClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                cont.resume(true)
            }
            addOnFailureListener {
                Log.e(TAG, "Deleting geofences failed with: ${it.message}")
                cont.resume(false)
            }
        }
    }

}
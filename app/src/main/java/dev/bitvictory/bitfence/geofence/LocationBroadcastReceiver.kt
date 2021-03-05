/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.bitvictory.bitfence.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import dev.bitvictory.bitfence.data.GeofenceDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import dev.bitvictory.bitfence.data.LocationEvent
import java.time.Instant
import java.util.*


/*
 * Triggered by the Geofence.  Since we only have one active Geofence at once, we pull the request
 * ID from the first Geofence, and locate it within the registered landmark data in our
 * GeofencingConstants within GeofenceUtils, which is a linear string search. If we had  very large
 * numbers of Geofence possibilities, it might make sense to use a different data structure.  We
 * then pass the Geofence index into the notification, which allows us to have a custom "found"
 * message associated with each Geofence.
 */
class LocationBroadcastReceiver() : BroadcastReceiver() {

    @ExperimentalMaterialApi
    override fun onReceive(context: Context, intent: Intent) {
        val database = GeofenceDatabase.getDatabase(context)
        val locationEventDao = database.locationEventsDao()
        val b = intent.extras

        val loc: Location? = b?.get(LocationManager.KEY_LOCATION_CHANGED) as Location?
        Log.d(TAG, "Received Location event for location with ${loc.toString()}")

        if (loc != null) {

            GlobalScope.launch {
                val event = LocationEvent(
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    accuracy = loc.accuracy,
                    date = Date.from(Instant.ofEpochSecond(loc.time))
                )

                locationEventDao.insert(event)
            }
        }
    }
}

private const val TAG = "LocationBroadcastReceiver"

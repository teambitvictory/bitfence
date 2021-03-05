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

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import dev.bitvictory.bitfence.R
import dev.bitvictory.bitfence.data.Event
import dev.bitvictory.bitfence.data.EventType
import dev.bitvictory.bitfence.data.GeofenceDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
 * Triggered by the Geofence.  Since we only have one active Geofence at once, we pull the request
 * ID from the first Geofence, and locate it within the registered landmark data in our
 * GeofencingConstants within GeofenceUtils, which is a linear string search. If we had  very large
 * numbers of Geofence possibilities, it might make sense to use a different data structure.  We
 * then pass the Geofence index into the notification, which allows us to have a custom "found"
 * message associated with each Geofence.
 */
class GeofenceBroadcastReceiver() : BroadcastReceiver() {

    @ExperimentalMaterialApi
    override fun onReceive(context: Context, intent: Intent) {
        val database = GeofenceDatabase.getDatabase(context)
        val eventsDao = database.eventsDao()
        val locationsDao = database.locationsDao()

        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            val locationId = getLocationIdFromGeofences(geofencingEvent.triggeringGeofences)

            if (geofencingEvent.hasError()) {
                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)

                GlobalScope.launch {
                    val location = if (locationId > -1) {
                        locationsDao.getLocationById(locationId).name
                    } else {
                        ""
                    }
                    val event = Event(
                        locationId = locationId,
                        eventType = EventType.ENTER,
                        locationName = location,
                        error = errorMessage
                    )

                    eventsDao.insert(event)
                }
                return
            }

            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.v(TAG, context.getString(R.string.geofence_entered))
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                GlobalScope.launch {
                    val location = if (locationId > -1) {
                        locationsDao.getLocationById(locationId).name
                    } else {
                        ""
                    }
                    val event = Event(
                        locationId = locationId,
                        eventType = EventType.ENTER,
                        locationName = location
                    )
                    eventsDao.insert(event)
                }

                notificationManager.sendGeofenceNotification(context, context.getString(R.string.geofence_entered))
            } else if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                Log.v(TAG, context.getString(R.string.geofence_dwell))
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                GlobalScope.launch {
                    val location = if (locationId > -1) {
                        locationsDao.getLocationById(locationId).name
                    } else {
                        ""
                    }
                    val event = Event(
                        locationId = locationId,
                        eventType = EventType.DWELL,
                        locationName = location
                    )
                    eventsDao.insert(event)
                }

                notificationManager.sendGeofenceNotification(context, context.getString(R.string.geofence_dwell))
            } else if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.v(TAG, context.getString(R.string.geofence_exited))
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                GlobalScope.launch {
                    val location = if (locationId > -1) {
                        locationsDao.getLocationById(locationId).name
                    } else {
                        ""
                    }
                    val event = Event(
                        locationId = locationId,
                        eventType = EventType.EXIT,
                        locationName = location
                    )
                    eventsDao.insert(event)
                }

                notificationManager.sendGeofenceNotification(context, context.getString(R.string.geofence_exited))
            }
        }
    }
}

private const val TAG = "GeofenceReceiver"

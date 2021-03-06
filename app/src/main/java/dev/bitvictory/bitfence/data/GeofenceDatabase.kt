/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.bitvictory.bitfence.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The [RoomDatabase] we use in this app.
 */
@Database(
    entities = [
        Location::class,
        Event::class,
        LocationEvent::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GeofenceDatabase : RoomDatabase() {
    abstract fun locationsDao(): LocationsDao
    abstract fun eventsDao(): EventsDao
    abstract fun locationEventsDao(): LocationEventDao

    companion object {
        @Volatile
        private var INSTANCE: GeofenceDatabase? = null

        fun getDatabase(context: Context): GeofenceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeofenceDatabase::class.java,
                    "geofence_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}

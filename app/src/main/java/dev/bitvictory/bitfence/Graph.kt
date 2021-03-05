package dev.bitvictory.bitfence

import android.content.Context
import dev.bitvictory.bitfence.data.GeofenceDatabase

object Graph {
    lateinit var database: GeofenceDatabase
        private set

    val locationsDao by lazy {
        database.locationsDao()
    }

    val eventsDao by lazy {
        database.eventsDao()
    }

    val locationEventsDao by lazy {
        database.locationEventsDao()
    }

    fun provide(context: Context) {
        database = GeofenceDatabase.getDatabase(context)
    }
}

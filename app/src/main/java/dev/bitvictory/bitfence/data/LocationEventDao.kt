package dev.bitvictory.bitfence.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [Location] related operations.
 */
@Dao
interface LocationEventDao {

    @Query("SELECT * FROM locationevents ORDER BY date DESC")
    fun getAllLocationEvents(): Flow<List<LocationEvent>>

    @Insert
    suspend fun insert(locationEvent: LocationEvent)

    @Query("DELETE FROM locationevents")
    suspend fun deleteAll()


}

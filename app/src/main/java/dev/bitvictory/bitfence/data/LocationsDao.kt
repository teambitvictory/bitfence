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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [Location] related operations.
 */
@Dao
interface LocationsDao {

    @Query("SELECT * FROM locations ORDER BY latitude DESC")
    fun getAllLocations(): Flow<List<Location>>

    @Query("SELECT * FROM locations")
    suspend fun getLocationList(): List<Location>

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocationById(id: Long): Location

    @Insert
    suspend fun insert(location: Location): Long

    @Query("DELETE FROM locations")
    fun deleteAll()

}

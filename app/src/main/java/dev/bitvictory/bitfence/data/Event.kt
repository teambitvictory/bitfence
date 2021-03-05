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

import androidx.compose.runtime.Immutable
import androidx.room.*
import java.util.*

@Entity(
    tableName = "events",
)
@Immutable
@TypeConverters(EventConverter::class, DateConverter::class)
data class Event(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "locationId") val locationId: Long,
    @ColumnInfo(name = "locationName") val locationName: String,
    @ColumnInfo(name = "eventType") val eventType: EventType,
    @ColumnInfo(name = "error") val error: String = "",
    @ColumnInfo(name = "date") val date: Date = Calendar.getInstance().time,
)

enum class EventType {
    ENTER, EXIT, DWELL, ERROR
}

class EventConverter {
    @TypeConverter
    fun toEventType(value: Int) = enumValues<EventType>()[value]

    @TypeConverter
    fun fromEventType(value: EventType) = value.ordinal
}

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

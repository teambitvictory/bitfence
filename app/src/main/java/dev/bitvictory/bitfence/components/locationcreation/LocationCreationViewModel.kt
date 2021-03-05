package dev.bitvictory.bitfence.components.locationcreation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bitvictory.bitfence.Graph
import dev.bitvictory.bitfence.data.Location
import dev.bitvictory.bitfence.data.LocationsDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class LocationCreationViewModel(
    private val locationsDao: LocationsDao = Graph.locationsDao
) : ViewModel() {

    // Holds our current longitude
    private val currentLongitude = MutableStateFlow("")

    // Holds our current latitude
    private val currentLatitude = MutableStateFlow("")

    // Holds our current name
    private val currentName = MutableStateFlow("")

    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(LocationCreationViewState())

    private var onNewLocationListener: (location: Location) -> Unit = {}

    val state: StateFlow<LocationCreationViewState>
        get() = _state

    init {
        viewModelScope.launch {
            // Combines the latest value from each of the flows, allowing us to generate a
            // view state instance which only contains the latest values.
            combine(
                currentLongitude,
                currentLatitude,
                currentName
            ) { currentLongitude, currentLatitude, currentName ->
                LocationCreationViewState(
                    name = currentName,
                    latitude = currentLatitude,
                    longitude = currentLongitude,
                )
            }.catch { throwable ->
                // TODO: emit a UI error here. For now we'll just rethrow
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun onLongitudeChanged(longitude: String) {
        currentLongitude.value = longitude
    }

    fun onLatitudeChanged(latitude: String) {
        currentLatitude.value = latitude
    }

    fun onNameChanged(name: String) {
        currentName.value = name
    }

    fun insertLocation() {
        val location = Location(
            name = state.value.name,
            longitude = state.value.longitude.toDouble(),
            latitude = state.value.latitude.toDouble()
        )
        Log.d("MainViewModel", "Create location $location")
        viewModelScope.launch {
            val id = locationsDao.insert(location)
            onNewLocationListener(location.copy(id = id))
        }
    }

    fun setOnNewLocation(onNewLocation: (location: Location) -> Unit) {
        this.onNewLocationListener = onNewLocation
    }

}

data class LocationCreationViewState(
    val name: String = "",
    val latitude: String = "",
    val longitude: String = ""
)

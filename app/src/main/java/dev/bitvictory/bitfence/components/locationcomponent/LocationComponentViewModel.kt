package dev.bitvictory.bitfence.components.locationcomponent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bitvictory.bitfence.Graph
import dev.bitvictory.bitfence.data.Location
import dev.bitvictory.bitfence.data.LocationsDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class LocationComponentViewModel(
    private val locationsDao: LocationsDao = Graph.locationsDao
) : ViewModel() {

    private val _state = MutableStateFlow(LocationComponentViewState())

    val state: StateFlow<LocationComponentViewState>
        get() = _state

    init {
        viewModelScope.launch {
            val locationListFlow = locationsDao.getAllLocations()

            locationListFlow.collect { _state.value = LocationComponentViewState(it) }
        }
    }

}

data class LocationComponentViewState(
    val locationList: List<Location> = emptyList()
)


package dev.bitvictory.bitfence.components.locationeventcomponent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bitvictory.bitfence.Graph
import dev.bitvictory.bitfence.data.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class LocationEventComponentViewModel(
    private val locationEventDao: LocationEventDao = Graph.locationEventsDao
) : ViewModel() {

    private val _state = MutableStateFlow(LocationEventComponentViewState())

    val state: StateFlow<LocationEventComponentViewState>
        get() = _state

    init {
        viewModelScope.launch {
            val locationListFlow = locationEventDao.getAllLocationEvents()

            locationListFlow.collect { _state.value = LocationEventComponentViewState(it) }
        }
    }

}

data class LocationEventComponentViewState(
    val eventList: List<LocationEvent> = emptyList()
)


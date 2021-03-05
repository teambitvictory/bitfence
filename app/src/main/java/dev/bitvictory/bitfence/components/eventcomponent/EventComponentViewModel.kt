package dev.bitvictory.bitfence.components.eventcomponent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bitvictory.bitfence.Graph
import dev.bitvictory.bitfence.data.Event
import dev.bitvictory.bitfence.data.EventsDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class EventComponentViewModel(
    private val eventsDao: EventsDao = Graph.eventsDao
) : ViewModel() {

    private val _state = MutableStateFlow(EventComponentViewState())

    val state: StateFlow<EventComponentViewState>
        get() = _state

    init {
        viewModelScope.launch {
            val locationListFlow = eventsDao.getAllEvents()

            locationListFlow.collect { _state.value = EventComponentViewState(it) }
        }
    }

}

data class EventComponentViewState(
    val eventList: List<Event> = emptyList()
)


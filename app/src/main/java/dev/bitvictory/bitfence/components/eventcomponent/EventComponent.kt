package dev.bitvictory.bitfence.components.eventcomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.bitvictory.bitfence.data.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun EventList() {
    val viewModel: EventComponentViewModel = viewModel()

    val viewState = viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        if (viewState.value.eventList.isEmpty()) {
            NoEvents()
        } else {
            LazyColumn(modifier = Modifier.padding(bottom = 64.dp)) {
                items(viewState.value.eventList) { EventItem(event = it) }
            }
        }
    }
}

@Composable
fun NoEvents() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "No events available", style = MaterialTheme.typography.body1, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun EventItem(event: Event) {
    val typography = MaterialTheme.typography
    Column {
        if(event.locationName.isNotBlank()) { Text(text = event.locationName, style = typography.h6) }
        Row {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Id", style = typography.body2)
                Text(text = "locationId", style = typography.body2)
                Text(text = "Event Type", style = typography.body2)
                if (event.error.isNotBlank()) { Text(text = "Error", style = typography.body2) }
                Text(text = "Date", style = typography.body2)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = event.id.toString(), style = typography.body2)
                Text(text = event.locationId.toString(), style = typography.body2)
                Text(text = event.eventType.name, style = typography.body2)
                if (event.error.isNotBlank()) { Text(text = event.error, style = typography.body2) }
                Text(text = event.date.toString(), style = typography.body2)
            }
        }
    }
}
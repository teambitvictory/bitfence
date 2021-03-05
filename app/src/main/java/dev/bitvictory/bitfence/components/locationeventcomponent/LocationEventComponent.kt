package dev.bitvictory.bitfence.components.locationeventcomponent

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
import dev.bitvictory.bitfence.data.LocationEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun LocationEventList() {
    val viewModel: LocationEventComponentViewModel = viewModel()

    val viewState = viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        if (viewState.value.eventList.isEmpty()) {
            NoLocationEvents()
        } else {
            LazyColumn(modifier = Modifier.padding(bottom = 64.dp)) {
                items(viewState.value.eventList) { LocationEventItem(event = it) }
            }
        }
    }
}

@Composable
fun NoLocationEvents() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "No events available", style = MaterialTheme.typography.body1, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun LocationEventItem(event: LocationEvent) {
    val typography = MaterialTheme.typography
    Column {
        Row {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Id", style = typography.body2)
                Text(text = "latitude", style = typography.body2)
                Text(text = "longitude", style = typography.body2)
                Text(text = "accuracy", style = typography.body2)
                Text(text = "Date", style = typography.body2)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = event.id.toString(), style = typography.body2)
                Text(text = event.latitude.toString(), style = typography.body2)
                Text(text = event.longitude.toString(), style = typography.body2)
                Text(text = event.accuracy.toString(), style = typography.body2)
                Text(text = event.date.toString(), style = typography.body2)
            }
        }
    }
}
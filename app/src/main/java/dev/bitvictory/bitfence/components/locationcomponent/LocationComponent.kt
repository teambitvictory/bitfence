package dev.bitvictory.bitfence.components.locationcomponent

import androidx.compose.foundation.layout.*
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
import dev.bitvictory.bitfence.data.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun LocationList() {
    val viewModel: LocationComponentViewModel = viewModel()

    val viewState = viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        if (viewState.value.locationList.isEmpty()) {
            NoLocations()
        } else {
            LazyColumn(modifier = Modifier.padding(bottom = 64.dp)) {
                items(viewState.value.locationList) { LocationItem(location = it) }
            }
        }
    }
}

@Composable
fun NoLocations() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "No locations available",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun LocationItem(location: Location) {
    val typography = MaterialTheme.typography
    Column {
        Text(text = location.name, style = typography.h6)
        Row {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Id", style = typography.body2)
                Text(text = "Latitude", style = typography.body2)
                Text(text = "Longitude", style = typography.body2)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = location.id.toString(), style = typography.body2)
                Text(text = location.latitude.toString(), style = typography.body2)
                Text(text = location.longitude.toString(), style = typography.body2)
            }
        }
    }
}
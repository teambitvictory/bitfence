package dev.bitvictory.bitfence.components.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import dev.bitvictory.bitfence.MainViewModel
import dev.bitvictory.bitfence.components.eventcomponent.EventList
import dev.bitvictory.bitfence.components.locationcomponent.LocationList
import dev.bitvictory.bitfence.components.locationcreation.CreationForm
import dev.bitvictory.bitfence.components.locationeventcomponent.LocationEventList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun MainComponent(navController: NavHostController, mainViewModel: MainViewModel) {
    val fabShape = CircleShape
    Scaffold(
        bottomBar = { AppBar(fabShape, navController) },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = { ActionButton(fabShape, navController) }
    ) {
        NavHost(navController = navController, startDestination = "locations") {
            composable("events") { EventScreen() }
            composable("locations") { LocationScreen(mainViewModel) }
            composable("locationevent") { LocationEventScreen(mainViewModel) }
            composable("create") { CreationForm(navController, mainViewModel::onNewLocation) }
        }
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun EventScreen() {
    Column(modifier = Modifier
        .padding(16.dp)) {
        Text(
            text = "Events",
            style = MaterialTheme.typography.h5,
        )
        EventList()
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun LocationScreen(mainViewModel: MainViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Locations",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(end = 16.dp)
            )
            OutlinedButton(onClick = { mainViewModel.refreshAllGeofences() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Refresh,
                        "Refresh Geofences",
                        Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = "Refresh all Geofences",
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
        }
        LocationList()
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun LocationEventScreen(mainViewModel: MainViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Location Events",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(end = 16.dp)
            )
            OutlinedButton(onClick = { mainViewModel.deleteAllLocationEvents() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Delete,
                        "Delete all Icon",
                        Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = "Delete all",
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
        }
        LocationEventList()
    }
}

@Composable
fun AppBar(fabShapes: Shape, navController: NavController) {
    BottomAppBar(cutoutShape = fabShapes) {
        Icon(
            Icons.Filled.List,
            "List of Events",
            Modifier
                .padding(start = 15.dp)
                .clickable(onClick = {
                    navController.navigate("events")
                })
        )
        Icon(
            Icons.Filled.LocationOn,
            "Locations",
            Modifier
                .padding(start = 15.dp)
                .clickable(onClick = {
                    navController.navigate("locations")
                })
        )
        Icon(
            Icons.Filled.Info,
            "Location Events",
            Modifier
                .padding(start = 15.dp)
                .clickable(onClick = {
                    navController.navigate("locationevent")
                })
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun ActionButton(fabShape: Shape, navController: NavController) {
    FloatingActionButton(
        shape = fabShape,
        onClick = { navController.navigate("create") }
    ) {
        Icon(Icons.Filled.Add, "Add a Location")
    }
}
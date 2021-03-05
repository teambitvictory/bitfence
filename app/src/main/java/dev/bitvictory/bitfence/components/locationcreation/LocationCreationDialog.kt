package dev.bitvictory.bitfence.components.locationcreation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import dev.bitvictory.bitfence.data.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun CreationForm(navController: NavController, onNewLocation: (location: Location) -> Unit) {

    val viewModel: LocationCreationViewModel = viewModel()
    viewModel.setOnNewLocation(onNewLocation)
    val keyboardController = LocalSoftwareKeyboardController.current

    val viewState = viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        val focusRequesterNameLat = remember { FocusRequester() }
        val focusRequesterLatLong = remember { FocusRequester() }
        Text(
            text = "Create a Geofence",
            style = MaterialTheme.typography.h5,
        )
        TextField(
            value = viewState.value.name,
            onValueChange = { viewModel.onNameChanged(it) },
            label = { Text(text = "Name") },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = { focusRequesterNameLat.requestFocus() }
            )
        )
        TextField(
            value = viewState.value.latitude,
            onValueChange = { viewModel.onLatitudeChanged(it) },
            label = { Text(text = "Latitude") },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequesterNameLat),
            maxLines = 1,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = { focusRequesterLatLong.requestFocus() }
            )
        )
        TextField(
            value = viewState.value.longitude,
            onValueChange = { viewModel.onLongitudeChanged(it) },
            label = { Text(text = "Longitude") },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequesterLatLong),
            maxLines = 1,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hideSoftwareKeyboard() }
            )
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.padding(8.dp), onClick = {
                    viewModel.onNameChanged("")
                    viewModel.onLongitudeChanged("")
                    viewModel.onLongitudeChanged("")
                    navController.navigate("locations")
                }) {
                Text(text = "Cancel", modifier = Modifier.padding(6.dp))
            }
            Button(modifier = Modifier.padding(8.dp), onClick = {
                viewModel.insertLocation()
                viewModel.onNameChanged("")
                viewModel.onLongitudeChanged("")
                viewModel.onLongitudeChanged("")
                navController.navigate("locations")
            }) {
                Text(text = "Add Geofence", modifier = Modifier.padding(6.dp))
            }
        }
    }

}

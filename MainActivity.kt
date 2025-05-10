package com.example.bluud

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluud.ui.theme.BluudTheme
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            BluudTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("donor_locator") {
                            DonorLocatorScreen()
                        }
                        composable("donation_scheduler") {
                            DonationSchedulerScreen()
                        }
                        composable("request_blood") {
                            RequestBloodScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Button(onClick = {
                navController.navigate("donor_locator")
                // TODO: Add logic for donor locator action
            }) {
                Text("Find Where I Can Donate and When")
            }
            Text("Locate nearby donation centers and view available time slots.")
        }

        Column {
            Button(onClick = {
                navController.navigate("donation_scheduler")
                // TODO: Add logic for donation scheduler action
            }) {
                Text("Schedule a Donation")
            }
            Text("Pick a date and time to book your next blood donation.")
        }

        Column {
            Button(onClick = {
                navController.navigate("request_blood")
                // TODO: Add logic for request blood action
            }) {
                Text("Request Blood")
            }
            Text("Submit a blood request for yourself or someone in need.")
        }
    }
}
@Composable
fun DonorLocatorScreen() {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    // Fetch last known location
    LaunchedEffect(Unit) {
        getLastKnownLocation(context) { location ->
            currentLocation = location
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        currentLocation?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 14f
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Nearby Donation Centers", style = MaterialTheme.typography.headlineSmall)

        // Check if location is available
        currentLocation?.let { location ->
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    position = LatLng(location.latitude, location.longitude),
                    title = "You are here"
                )
                // TODO: Add other donation center markers here
            }
        } ?: Text("Detecting your location...")

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            // TODO: Trigger location refresh and fetch new data
        }) {
            Text("Refresh Nearby Locations")
        }
    }
}

@SuppressLint("MissingPermission")
fun getLastKnownLocation(context: Context, onLocationFound: (Location?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            onLocationFound(location)
        }
        .addOnFailureListener {
            onLocationFound(null)
        }

@Composable
fun DonationSchedulerScreen() {
    Text("Donation Scheduler Screen")
}

@Composable
fun RequestBloodScreen() {
    Text("Request Blood Screen")
}@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BluudTheme {
        HomeScreen(navController = rememberNavController())
    }
}

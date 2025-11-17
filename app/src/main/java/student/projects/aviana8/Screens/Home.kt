package student.projects.aviana8.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import student.projects.aviana8.Services.BirdHotspot
import student.projects.aviana8.Services.NotableBirdSightings
import student.projects.aviana8.Viewmodels.HomeViewModel
import student.projects.aviana8.Viewmodels.LocationState
import student.projects.aviana8.Viewmodels.NetworkResponse
import student.projects.aviana8.Viewmodels.ProfileViewModel
import student.projects.aviana8.ui.theme.Brown
import student.projects.aviana8.ui.theme.GreenOutline
import student.projects.aviana8.ui.theme.Peach
import student.projects.aviana8.ui.theme.TextBrown
import student.projects.aviana8.ui.theme.WhiteNew

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val hotspotsState by homeViewModel.hotspotsState.collectAsStateWithLifecycle()
    val sightingsState by homeViewModel.sightingsState.collectAsStateWithLifecycle()
    val networkState by homeViewModel.networkState.collectAsStateWithLifecycle()
    val locationState by homeViewModel.locationState.collectAsStateWithLifecycle()
    val cacheInfo by homeViewModel.cacheInfo.collectAsStateWithLifecycle()

    var showOfflineAlert by remember { mutableStateOf(false) }
    var showLocationError by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var context2 = LocalContext.current

    // Show offline alert when refreshing without network
    LaunchedEffect(networkState, isRefreshing) {
        if (isRefreshing && !networkState) {
            showOfflineAlert = true
            isRefreshing = false
        }
    }

    // Show location error if location fails
    LaunchedEffect(locationState) {
        if (locationState is LocationState.Error) {
            showLocationError = true
        }
    }

    // Handle swipe refresh completion
    LaunchedEffect(hotspotsState, sightingsState) {
        if (hotspotsState !is NetworkResponse.Loading &&
            sightingsState !is NetworkResponse.Loading) {
            isRefreshing = false
        }
    }

    if (showOfflineAlert) {
        AlertDialog(
            onDismissRequest = { showOfflineAlert = false },
            title = {
                Text(
                    text = "Offline Mode",
                    color = TextBrown,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "You're currently offline. Showing cached data. " +
                            "Connect to internet to refresh with latest hotspots and sightings.",
                    color = TextBrown.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showOfflineAlert = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Peach,
                        contentColor = Brown
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showLocationError) {
        AlertDialog(
            onDismissRequest = { showLocationError = false },
            title = {
                Text(
                    text = "Location Issue",
                    color = TextBrown,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Unable to get your current location:",
                        color = TextBrown.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (locationState as? LocationState.Error)?.message ?: "Unknown error",
                        color = TextBrown.copy(alpha = 0.6f),
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Using default location for now. Pull to refresh to try again.",
                        color = TextBrown.copy(alpha = 0.8f)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLocationError = false
                        homeViewModel.viewModelScope.launch(Dispatchers.IO) {
                            if (ActivityCompat.checkSelfPermission(
                                    context2,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                                    context2,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return@launch
                            }
                            homeViewModel.getCurrentLocation()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Peach,
                        contentColor = Brown
                    )
                ) {
                    Text("Retry Location")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLocationError = false }
                ) {
                    Text("Dismiss", color = TextBrown)
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteNew),
        topBar = {
           /* HomeAppBar(
                cacheInfo = cacheInfo,
                onRefresh = {
                    isRefreshing = true
                    homeViewModel.refreshAllData()
                }
            )*/
        },
        floatingActionButton = {
            // Location FAB to manually refresh location
            FloatingActionButton(
                onClick = {
                    isRefreshing = true
                    homeViewModel.getCurrentLocation()
                },
                containerColor = Peach,
                contentColor = Brown
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Refresh Location"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WhiteNew)
        ) {
            when (locationState) {
                is LocationState.Loading -> {
                    LocationLoadingState()
                }
                is LocationState.Error -> {
                    // Still try to show data with fallback location
                    when {
                        hotspotsState is NetworkResponse.Loading && isRefreshing -> {
                            //LoadingState()
                        }
                        hotspotsState is NetworkResponse.Error -> {
                           /* ErrorState(
                                errorMessage = (hotspotsState as NetworkResponse.Error).message,
                                isOnline = networkState,
                                onRetry = { homeViewModel.refreshAllData() }
                            )*/
                        }
                        hotspotsState is NetworkResponse.Success -> {
                            HotspotsList(
                                hotspots = (hotspotsState as NetworkResponse.Success).data ?: emptyList(),
                                sightings = (sightingsState as? NetworkResponse.Success)?.data ?: emptyList(),
                                isOnline = networkState,
                                locationState = locationState,
                                onRefresh = {
                                    isRefreshing = true
                                    homeViewModel.refreshAllData()
                                },
                                isRefreshing = isRefreshing
                            )
                        }
                        else -> {
                           /* EmptyState(
                                isOnline = networkState,
                                onRefresh = { homeViewModel.refreshAllData() }
                            )*/
                        }
                    }
                }
                is LocationState.Success -> {
                    when {
                        hotspotsState is NetworkResponse.Loading && isRefreshing -> {
                          //  LoadingState()
                        }
                        hotspotsState is NetworkResponse.Error -> {
                           /* ErrorState(
                                errorMessage = (hotspotsState as NetworkResponse.Error).message,
                                isOnline = networkState,
                                onRetry = { homeViewModel.refreshAllData() }
                            )*/
                        }
                        hotspotsState is NetworkResponse.Success -> {
                            HotspotsList(
                                hotspots = (hotspotsState as NetworkResponse.Success).data ?: emptyList(),
                                sightings = (sightingsState as? NetworkResponse.Success)?.data ?: emptyList(),
                                isOnline = networkState,
                                locationState = locationState,
                                onRefresh = {
                                    isRefreshing = true
                                    homeViewModel.refreshAllData()
                                },
                                isRefreshing = isRefreshing
                            )
                        }
                        else -> {
                           /* EmptyState(
                                isOnline = networkState,
                                onRefresh = { homeViewModel.refreshAllData() }
                            )*/
                        }
                    }
                }
            }

            // Offline indicator
            if (!networkState) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Peach.copy(alpha = 0.9f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WifiOff,
                                contentDescription = "Offline",
                                tint = Brown,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Offline Mode",
                                color = Brown,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteNew),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = Peach,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Getting your location...",
                color = TextBrown,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Finding nearby bird hotspots",
                color = TextBrown.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun HotspotsList(
    hotspots: List<BirdHotspot>,
    sightings: List<NotableBirdSightings>,
    isOnline: Boolean,
    locationState: LocationState,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    val context = LocalContext.current

    // Get current coordinates for distance calculation
    val currentLocation = when (locationState) {
        is LocationState.Success -> Pair(locationState.lat, locationState.lng)
        else -> null
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteNew),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location status card
            item {
                when (locationState) {
                    is LocationState.Success -> {
                        LocationSuccessCard(
                            lat =  locationState.lat!!,
                            lng = locationState.lng!!,
                            hotspotCount = hotspots.size
                        )
                    }
                    is LocationState.Error -> {
                        LocationErrorCard(
                            errorMessage = locationState.message,
                            onRetry = onRefresh
                        )
                    }
                    else -> {
                        // Shouldn't happen here, but just in case
                    }
                }
            }

            // Offline notice
            if (!isOnline) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = GreenOutline.copy(alpha = 0.1f)
                        ),
                        border = BorderStroke(1.dp, GreenOutline.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = "Offline",
                                tint = GreenOutline,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Showing cached data. Connect to internet for latest updates.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextBrown.copy(alpha = 0.8f),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Notable Sightings Section
            if (sightings.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Notable Sightings",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextBrown,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

             /*   items(sightings.take(5)) { sighting ->
                    NotableSightingCard(
                        sighting = sighting,
                        isOnline = isOnline,
                        currentLocation = currentLocation
                    )
                }*/

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = GreenOutline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Hotspots Section
            item {
                Text(
                    text = "Nearby Bird Hotspots",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = if (isOnline) {
                        "${hotspots.size} hotspots found near you"
                    } else {
                        "${hotspots.size} cached hotspots"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBrown.copy(alpha = 0.7f)
                )
            }

            // Sort hotspots by distance if we have current location
            val sortedHotspots = if (currentLocation != null) {
                hotspots.sortedBy { hotspot ->
                    calculateDistance(
                        currentLocation.first!!, currentLocation.second!!,
                        hotspot.lat, hotspot.lng
                    )
                }
            } else {
                hotspots
            }

            items(sortedHotspots) { hotspot ->
                HotspotCard(
                    hotspot = hotspot,
                    isOnline = isOnline,
                    currentLocation = currentLocation as Pair<Double, Double>?
                )
            }
        }
    }
}

@Composable
fun LocationSuccessCard(lat: Double?, lng: Double, hotspotCount: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = GreenOutline.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, GreenOutline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = GreenOutline,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Your Location",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${"%.4f".format(lat)}, ${"%.4f".format(lng)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextBrown.copy(alpha = 0.7f)
                )
                Text(
                    text = "$hotspotCount hotspots within 50km",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextBrown.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun LocationErrorCard(errorMessage: String, onRetry: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Peach.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, Peach.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOff,
                    contentDescription = "Location Error",
                    tint = Peach,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Location Issue",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = TextBrown.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Peach,
                    contentColor = Brown
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Retry Location", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun HotspotCard(
    hotspot: BirdHotspot,
    isOnline: Boolean,
    currentLocation: Pair<Double, Double>?
) {
    val distance = currentLocation?.let { (userLat, userLng) ->
        calculateDistance(userLat, userLng, hotspot.lat, hotspot.lng)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = WhiteNew
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, GreenOutline.copy(alpha = 0.3f)),
        onClick = {
            // TODO: Navigate to hotspot detail
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = hotspot.locName,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextBrown,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                if (!isOnline) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = "Cached",
                        tint = TextBrown.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${hotspot.numSpeciesAllTime} species",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBrown.copy(alpha = 0.8f)
                    )



                    if (distance != null) {
                        Text(
                            text = "Distance: ${"%.1f".format(distance / 1000)} km",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextBrown.copy(alpha = 0.6f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(Peach, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when {
                            distance != null && distance < 1000 -> "${distance.toInt()}m"
                            distance != null -> "${"%.1f".format(distance / 1000)}km"
                            isOnline -> "Nearby"
                            else -> "Cached"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = Brown,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Utility function to calculate distance between two coordinates
private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lng1, lat2, lng2, results)
    return results[0]
}
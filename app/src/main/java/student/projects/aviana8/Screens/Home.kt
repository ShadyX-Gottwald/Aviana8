package student.projects.aviana8.Screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import student.projects.aviana8.Services.BirdHotspot
import student.projects.aviana8.Services.HotspotRepository
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val isOnline = rememberNetworkState()


    // Load data when screen starts or network changes
    LaunchedEffect(isOnline) {
      //  homeViewModel.loadDataSimple(isOnline)
        homeViewModel.fetchNearbyHotspots()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Bird Hotspots")
                },
                actions = {
                    // Network status indicator
                    Icon(
                        imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                        contentDescription = if (isOnline) "Online" else "Offline",
                        tint = if (isOnline) Color.Green else Color.Red
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
                containerColor = Color.Blue
            ) {
                Icon(Icons.Default.Refresh, "Refresh")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Show loading
          /*  if (homeViewModel.isLoadingSimple.value) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading hotspots...")
                }
            }


            // Show error
            else if (homeViewModel.errorMessageSimple.value != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Error,
                        "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(homeViewModel.errorMessageSimple.value ?: "Unknown error")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {  }) {
                        Text("Retry")
                    }
                }
            }*/

            // Show hotspots list
          /*  else
                LazyColumn {
                    // Network status banner
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),


                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isOnline) Icons.Default.Wifi
                                    else Icons.Default.WifiOff,
                                    contentDescription = null,
                                    tint = if (isOnline) Color.Green else Color.Red
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isOnline) "Online - Live data"
                                    else "Offline - Cached data",
                                    color = if (isOnline) Color.Green else Color.Red
                                )
                            }
                        }
                    }

                    // Hotspots list
                    items(homeViewModel.hotspotsSimple.value) { hotspot ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = hotspot.locName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${hotspot.numSpeciesAllTime} species")
                                Text("Last seen: ${formatDate(hotspot.latestObsDt)}")

                                // Show cached badge if offline
                                if (!isOnline) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "📱 Cached",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }*/
            when(val result = homeViewModel.hotspotsState) {
                is NetworkResponse.Success<*> -> {
                    LazyColumn {
                        // Network status banner
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),


                                ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isOnline) Icons.Default.Wifi
                                        else Icons.Default.WifiOff,
                                        contentDescription = null,
                                        tint = if (isOnline) Color.Green else Color.Red
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isOnline) "Online - Live data"
                                        else "Offline - Cached data",
                                        color = if (isOnline) Color.Green else Color.Red
                                    )
                                }
                            }
                        }

                        // Hotspots list
                        items(homeViewModel.hotspotsSimple.value) { hotspot ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = hotspot.locName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${hotspot.numSpeciesAllTime} species")
                                    Text("Last seen: ${formatDate(hotspot.latestObsDt)}")

                                    // Show cached badge if offline
                                    if (!isOnline) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "📱 Cached",
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }


            }
        }
    }

// Simple date formatter
fun formatDate(dateString: String): String {
    return try {
        if (dateString.length >= 10) dateString.substring(0, 10) else dateString
    } catch (e: Exception) {
        dateString
    }
}





// NetworkMonitor.kt
@Composable
fun rememberNetworkState(): Boolean {
    val context = LocalContext.current
    val connectivityManager = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    val isOnline = remember { mutableStateOf(false) }

    LaunchedEffect(connectivityManager) {
        while (true) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            isOnline.value = capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    )
            delay(5000) // Check every 5 seconds
        }
    }

    return isOnline.value
}
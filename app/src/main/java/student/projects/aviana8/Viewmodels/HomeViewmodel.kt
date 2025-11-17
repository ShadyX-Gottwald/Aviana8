package student.projects.aviana8.Viewmodels

import android.Manifest
import android.Manifest.*
import android.Manifest.permission.*
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.projects.aviana8.Data.HotspotEntity
import student.projects.aviana8.Services.BirdHotspot
import student.projects.aviana8.Services.HotspotRepository
import student.projects.aviana8.Services.LocationResult
import student.projects.aviana8.Services.LocationService
import student.projects.aviana8.Services.NetworkMonitor
import student.projects.aviana8.Services.NotableBirdSightings
import student.projects.aviana8.Viewmodels.LocationState.*

// HomeViewModel.kt
//@RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION])
class HomeViewModel(
    private val hotspotRepository: HotspotRepository,
    private val locationService: LocationService,
  //  context: Context
   // application: Application
) : ViewModel() {

    // Hotspots state
    private val _hotspotsState = MutableStateFlow<NetworkResponse<List<BirdHotspot>>>(NetworkResponse.Idle)
    val hotspotsState: StateFlow<NetworkResponse<List<BirdHotspot>>> = _hotspotsState.asStateFlow()

    // Notable sightings state
    private val _sightingsState = MutableStateFlow<NetworkResponse<List<NotableBirdSightings>>>(NetworkResponse.Idle)
    val sightingsState: StateFlow<NetworkResponse<List<NotableBirdSightings>>> = _sightingsState.asStateFlow()

    // Network state
    private val _networkState = MutableStateFlow(true)
    val networkState: StateFlow<Boolean> = _networkState.asStateFlow()

    // Location state
    private val _locationState = MutableStateFlow<LocationState>(Loading)
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    // Cache info
    private val _cacheInfo = MutableStateFlow("")
    val cacheInfo: StateFlow<String> = _cacheInfo.asStateFlow()

    // Current location coordinates
    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)

    private val ebirdApiKey = "rhj2pqdjsgpu"
    //private val networkMonitor = NetworkMonitor(context)

    init {
        checkNetworkStatus()
        //getCurrentLocation()
        //startLocationUpdates()
    }

    // Simple states
    val hotspotsSimple = mutableStateOf<List<BirdHotspot>>(emptyList())
    val cachedDataSimple= mutableStateOf<List<HotspotEntity>>(emptyList())


    val isLoadingSimple = mutableStateOf(true)
    val errorMessageSimple = mutableStateOf<String?>(null)

    // Simple methods




    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    fun getCurrentLocation() {
        viewModelScope.launch ()
         {

            _locationState.value = Loading


               when(val result = locationService.getCurrentLocation()) {
                is LocationResult.Success -> {
                    val lat = result.location.result?.latitude
                    val lng = result.location.result?.longitude
                    _currentLocation.value = Pair(lat, lng) as Pair<Double, Double>?
                    _locationState.value = Success(lat, lng)

                    // Fetch data with new location
                    fetchNearbyHotspots()
                    fetchNotableSightings()
                    updateCacheInfo()
                }
                is LocationResult.Error -> {
                    _locationState.value = Error(result.message)
                    // Use default location as fallback
                    _currentLocation.value = Pair(40.7128, -74.0060) // New York
                    fetchNearbyHotspots()
                    fetchNotableSightings()
                }


            }
        }
    }

    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    private fun startLocationUpdates() {
        locationService.startLocationUpdates { location ->
            viewModelScope.launch {
                val newLat = location.latitude
                val newLng = location.longitude
                val current = _currentLocation.value

                // Only update if location changed significantly (more than 1km)
                if (current == null || calculateDistance(
                        current.first, current.second, newLat, newLng
                    ) > 1000
                ) {
                    _currentLocation.value = Pair(newLat, newLng)
                    _locationState.value = Success(newLat, newLng)

                    // Auto-refresh data when location changes significantly
                    if (_networkState.value) {
                        fetchNearbyHotspots(true)
                        fetchNotableSightings(true)
                    }
                }
            }
        }
    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }

    fun checkNetworkStatus() {
        //_networkState.value = networkMonitor.isConnected()
    }

    fun fetchNearbyHotspots(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            //checkNetworkStatus()

            val location = _currentLocation.value
            if (location == null) {
                _hotspotsState.value = NetworkResponse.Error("Location not available")
                return@launch
            }

            if (forceRefresh) {
                _hotspotsState.value = NetworkResponse.Loading
            }

            val (lat, lng) = location

            when (val result = hotspotRepository.getNearbyHotspots(lat, lng, ebirdApiKey)) {
                is NetworkResponse.Success -> {

                    _hotspotsState.value = NetworkResponse.Success(
                        result.data.getOrDefault(emptyList()))
                    hotspotsSimple.value = result.data.getOrDefault(emptyList())
                    updateCacheInfo()
                }
                else -> {
//                    var it = result.
//                    _hotspotsState.value = NetworkResponse.Success(it!!)
//                    updateCacheInfo()
                }
            }
        }
    }

    fun fetchNotableSightings(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            checkNetworkStatus()

            val location = _currentLocation.value
            if (location == null) {
                _sightingsState.value = NetworkResponse.Error("Location not available")
                return@launch
            }

            if (forceRefresh) {
                _sightingsState.value = NetworkResponse.Loading
            }

            val (lat, lng) = location

           /* when (val result = hotspotRepository.getNotableSightings(lat, lng, ebirdApiKey)) {
                is Result.Success -> {
                    _sightingsState.value = NetworkResponse.Success(result.data)
                    updateCacheInfo()
                }
                is Result.Failure -> {
                    _sightingsState.value = NetworkResponse.Error(result.exception.message ?: "Unknown error")
                    updateCacheInfo()
                }
            }*/
        }
    }

    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION])
    fun refreshAllData() {
        checkNetworkStatus()

        if (_networkState.value) {
            // Get fresh location and then fetch data
            getCurrentLocation()
        } else {
            // Offline: just reload from cache
            fetchNearbyHotspots()
            fetchNotableSightings()
        }
    }

    fun manualLocationUpdate(lat: Double, lng: Double) {
        _currentLocation.value = Pair(lat, lng)
        _locationState.value = Success(lat, lng)
        fetchNearbyHotspots(true)
        fetchNotableSightings(true)
    }

    private suspend fun updateCacheInfo() {
      //  val hotspotCount = hotspotRepository.getCachedHotspotsCount()
     //   val sightingCount = hotspotRepository.getCachedSightingsCount()
        val location = _currentLocation.value

        val locationInfo = if (location != null) {
            val (lat, lng) = location
            "📍 ${"%.4f".format(lat)}, ${"%.4f".format(lng)}"
        } else {
            "📍 Getting location..."
        }

        _cacheInfo.value = if (_networkState.value) {
            "$locationInfo • Online •  hotspots, and sightings cached"
        } else {
            "$locationInfo • Offline • Showing  cached hotspots, and cached sightings"
        }
    }
}

// Location state sealed class
sealed class LocationState {
    object Loading : LocationState()
    data class Success(val lat: Double?, val lng: Double?) : LocationState()
    data class Error(val message: String) : LocationState()
}

// Create a Factory
class HomeViewModelFactory(
    private val hotspotRepository: HotspotRepository,
    private val locationService: LocationService,

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(hotspotRepository,locationService  ) as T
    }
}
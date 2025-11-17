package student.projects.aviana8.Services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

// PermissionsManager.kt
class PermissionsManager {
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Notification permission not required below Android 13
        }
    }
}

class LocationService(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getCurrentLocation(): LocationResult {
        return try {
          /*  val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000 // 10 seconds
            )*/

            // Check if location permissions are granted
            if (hasLocationPermission()) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).let { location ->
                    LocationResult.Success(location)
                } ?: run {
                    // Fallback to last known location
                    getLastKnownLocation()
                }
            } else {
                LocationResult.Error("Location permission not granted")
            }
        } catch (e: Exception) {
            LocationResult.Error(e.message ?: "Failed to get location")
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun getLastKnownLocation(): LocationResult {
        return try {
            fusedLocationClient.lastLocation.let { location ->
                LocationResult.Success(location)
            } ?: LocationResult.Error("No last known location available")
        } catch (e: Exception) {
            LocationResult.Error(e.message ?: "Failed to get last known location")
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startLocationUpdates(callback: (Location) -> Unit) {
        if (!hasLocationPermission()) return

       /* val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            30000 // 30 seconds
        )*/

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: com.google.android.gms.location.LocationResult) {
               // locationResult.lastLocation?.let(callback)
            }
        }

        /*fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )*/
    }
}

sealed class LocationResult {
    data class Success(val location: Task<Location?>) : LocationResult()
    data class Error(val message: String) : LocationResult()
}

class NetworkMonitor(private val context: Context) {

    fun isConnected(): Boolean {
        val connectivityManager = this.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }
}
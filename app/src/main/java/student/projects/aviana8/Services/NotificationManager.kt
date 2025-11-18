package student.projects.aviana8.Services

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import student.projects.aviana8.Screens.Screen

class NotificationPermissionManager(private val context: Context) {

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // For older versions, permission is granted by default
        }
    }

    fun shouldShowRequestPermissionRationale(activity: android.app.Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            false
        }
    }

    fun requestNotificationPermission(activity: android.app.Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun openAppSettings(activity: android.app.Activity) {
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//            data = Uri.fromParts("package", activity.packageName, null)
//        }
//        activity.startActivity(intent)
    }

    fun areNotificationsEnabled(): Boolean {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager?.areNotificationsEnabled() ?: false
        } else {
            true // For older versions, assume enabled
        }
    }
}
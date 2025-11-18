package student.projects.aviana8.Services

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import androidx.core.content.edit

class FCMManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("bird_watch_prefs", Context.MODE_PRIVATE)

    suspend fun getFCMToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e("FCMManager", "Failed to get FCM token: ${e.message}")
            null
        }
    }

    fun saveFCMToken(token: String) {
        sharedPreferences.edit().putString("fcm_token", token).apply()
    }

    fun getStoredToken(): String? {
        return sharedPreferences.getString("fcm_token", null)
    }

    fun hasToken(): Boolean {
        return getStoredToken() != null
    }

    suspend fun subscribeToTopic(topic: String): Boolean {
        return try {
            FirebaseMessaging.getInstance().subscribeToTopic(topic).await()
            Log.d("FCMManager", "Subscribed to topic: $topic")
            true
        } catch (e: Exception) {
            Log.e("FCMManager", "Failed to subscribe to topic $topic: ${e.message}")
            false
        }
    }

    suspend fun unsubscribeFromTopic(topic: String): Boolean {
        return try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).await()
            Log.d("FCMManager", "Unsubscribed from topic: $topic")
            true
        } catch (e: Exception) {
            Log.e("FCMManager", "Failed to unsubscribe from topic $topic: ${e.message}")
            false
        }
    }

    suspend fun subscribeToLanguageTopic(languageCode: String) {
        // Unsubscribe from previous language topic
        val previousLanguage = sharedPreferences.getString("previous_language", null)
        previousLanguage?.let {
            unsubscribeFromTopic("language_$it")
        }

        // Subscribe to new language topic
        subscribeToTopic("language_$languageCode")
        sharedPreferences.edit { putString("previous_language", languageCode) }
    }
}
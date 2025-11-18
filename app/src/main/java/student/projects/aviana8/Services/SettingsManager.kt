package student.projects.aviana8.Services

import android.content.Context
import student.projects.aviana8.Data.UserPreferences

class SettingsManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("bird_watch_prefs", Context.MODE_PRIVATE)

    fun saveLanguage(languageCode: String) {
        sharedPreferences.edit().putString("language", languageCode).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString("language", "en") ?: "en"
    }

    fun saveTheme(theme: String) {
        sharedPreferences.edit().putString("theme", theme).apply()
    }

    fun getTheme(): String {
        return sharedPreferences.getString("theme", "light") ?: "light"
    }

    fun getUserPreferences(): UserPreferences {
        return UserPreferences(
            language = getLanguage(),
            theme = getTheme()
        )
    }
}
package student.projects.aviana8.Data

data class UserPreferences(
    val language: String = "en", // en, af, xh
    val theme: String = "light"
)

sealed class AppLanguage(val code: String, val displayName: String) {
    object English : AppLanguage("en", "English")
    object Afrikaans : AppLanguage("af", "Afrikaans")
    object Xhosa : AppLanguage("xh", "isiXhosa")

    companion object {
        fun fromCode(code: String): AppLanguage {
            return when (code) {
                "af" -> Afrikaans
                "xh" -> Xhosa
                else -> English
            }
        }

        val allLanguages = listOf(English, Afrikaans, Xhosa)
    }
}

// String resources for different languages
object AppStrings {
    // English strings
    private val englishStrings = mapOf(
        "app_name" to "Bird Watch",
        "profile_settings" to "Profile & Settings",
        "my_stats" to "My Bird Watching Stats",
        "saved_birds" to "Saved Birds",
        "achievements" to "Achievements",
        "language_settings" to "Language Settings",
        "no_birds_saved" to "No birds saved yet",
        "start_saving" to "Start saving birds to see them here",
        "no_achievements" to "No achievements yet",
        "save_more_birds" to "Save more birds to unlock achievements",
        "bird_watch" to "Bird Watch",
        "loading_birds" to "Loading birds...",
        "no_birds_found" to "No birds found",
        "retry" to "Retry",
        "save_to_my_birds" to "Save to My Birds",
        "saving" to "Saving...",
        "already_saved" to "Already Saved",
        "first_sighting" to "First Sighting",
        "bird_enthusiast" to "Bird Enthusiast",
        "bird_watcher" to "Bird Watcher",
        "bird_expert" to "Bird Expert",
        "save_first_bird" to "Save your first bird",
        "save_3_birds" to "Save 3 different birds",
        "save_5_birds" to "Save 5 different birds",
        "save_10_birds" to "Save 10 different birds",
        "achievement_unlocked" to "Achievement Unlocked!",
        "first_bird_saved" to "First bird saved! You're on your bird watching journey!",
        "bird_saved_success" to " saved successfully!",
        "delete" to "Delete",
        "saved_on" to "Saved on",
        "earned_on" to "Earned on",
        "save_to_unlock" to "Save birds to unlock",
        "birds_saved" to "birds saved",
        "bird_watching_progress" to "Bird Watching Progress",
        // English (add to englishStrings):
        "first_bird" to "First Sighting",
        "bird_enthusiast" to "Bird Enthusiast",
        "bird_watcher" to "Bird Watcher",
        "bird_expert" to "Bird Expert",
    )

    // Afrikaans strings
    private val afrikaansStrings = mapOf(
        "app_name" to "Voëlkyk",
        "profile_settings" to "Profiel & Instellings",
        "my_stats" to "My Voëlkyk Statistieke",
        "saved_birds" to "Gestoorde Voëls",
        "achievements" to "Prestasies",
        "language_settings" to "Taal Instellings",
        "no_birds_saved" to "Nog geen voëls gestoor nie",
        "start_saving" to "Begin voëls stoor om hulle hier te sien",
        "no_achievements" to "Nog geen prestasies nie",
        "save_more_birds" to "Stoor meer voëls om prestasies te ontgrendel",
        "bird_watch" to "Voëlkyk",
        "loading_birds" to "Laai voëls...",
        "no_birds_found" to "Geen voëls gevind nie",
        "retry" to "Probeer Weer",
        "save_to_my_birds" to "Stoor na My Voëls",
        "saving" to "Stoor...",
        "already_saved" to "Reeds Gestoor",
        "first_sighting" to "Eerste Waarneming",
        "bird_enthusiast" to "Voël-entoesias",
        "bird_watcher" to "Voëlkyker",
        "bird_expert" to "Voëldeskundige",
        "save_first_bird" to "Stoor jou eerste voël",
        "save_3_birds" to "Stoor 3 verskillende voëls",
        "save_5_birds" to "Stoor 5 verskillende voëls",
        "save_10_birds" to "Stoor 10 verskillende voëls",
        "achievement_unlocked" to "Prestasie Ontgrendel!",
        "first_bird_saved" to "Eerste voël gestoor! Jy is op jou voëlkyk reis!",
        "bird_saved_success" to " suksesvol gestoor!",
        "delete" to "Verwyder",
        "saved_on" to "Gestoor op",
        "earned_on" to "Verdien op",
        "save_to_unlock" to "Stoor voëls om te ontgrendel",
        "birds_saved" to "voëls gestoor",
        "bird_watching_progress" to "Voëlkyk Vordering",
        // Afrikaans (add to afrikaansStrings):
        "first_bird" to "Eerste Waarneming",
        "bird_enthusiast" to "Voël-entoesias",
        "bird_watcher" to "Voëlkyker",
        "bird_expert" to "Voëldeskundige",
    )

    // Xhosa strings
    private val xhosaStrings = mapOf(
        "app_name" to "Ukujonga Iintaka",
        "profile_settings" to "Iprofayile & Iisetingi",
        "my_stats" to "Amanani Omjongo Wentaka Yam",
        "saved_birds" to "Iintaka Ezilondoloziweyo",
        "achievements" to "Izifezile",
        "language_settings" to "Iisetingi Zolwimi",
        "no_birds_saved" to "Azikho iintaka ezilondoloziweyo okwangoku",
        "start_saving" to "Qala ukulondoloza iintaka ukuze uzibone apha",
        "no_achievements" to "Azikho izifezile okwangoku",
        "save_more_birds" to "Londoloza ezinye iintaka ukuze uvule izifezile",
        "bird_watch" to "Ukujonga Iintaka",
        "loading_birds" to "Ilayisha iintaka...",
        "no_birds_found" to "Azikho iintaka ezifunyenweyo",
        "retry" to "Zama Kwakhona",
        "save_to_my_birds" to "Londoloza KwiiNtaka Zam",
        "saving" to "Ilondoloza...",
        "already_saved" to "Selilondoloziwe",
        "first_sighting" to "Ukuqala Ukubona",
        "bird_enthusiast" to "Umthandi Weentaka",
        "bird_watcher" to "Umjongi Weentaka",
        "bird_expert" to "Ingcali Yeentaka",
        "save_first_bird" to "Londoloza intaka yakho yokuqala",
        "save_3_birds" to "Londoloza iintaka ezintathu ezohlukeneyo",
        "save_5_birds" to "Londoloza iintaka ezintlanu ezohlukeneyo",
        "save_10_birds" to "Londoloza iintaka eziilishumi ezohlukeneyo",
        "achievement_unlocked" to "Isifezile Sivuliwe!",
        "first_bird_saved" to "Intaka yokuqala ilondoloziwe! Usemkhankasweni wakho wokujonga iintaka!",
        "bird_saved_success" to " ilondoloziwe ngempumelelo!",
        "delete" to "Cima",
        "saved_on" to "Ilondoloziwe ngo",
        "earned_on" to "Kufunyenwe ngo",
        "save_to_unlock" to "Londoloza iintaka ukuze uvule",
        "birds_saved" to "iintaka ezilondoloziweyo",
        "bird_watching_progress" to "Inkqubela Yokujonga Iintaka",
        // Xhosa (add to xhosaStrings):
        "first_bird" to "Ukuqala Ukubona",
        "bird_enthusiast" to "Umthandi Weentaka",
        "bird_watcher" to "Umjongi Weentaka",
        "bird_expert" to "Ingcali Yeentaka"
    )

    fun getString(key: String, language: String): String {
        val stringMap = when (language) {
            "af" -> afrikaansStrings
            "xh" -> xhosaStrings
            else -> englishStrings
        }
        return stringMap[key] ?: englishStrings[key] ?: key
    }
}
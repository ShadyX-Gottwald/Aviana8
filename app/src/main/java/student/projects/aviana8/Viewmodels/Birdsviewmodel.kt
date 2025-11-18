package student.projects.aviana8.Viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import student.projects.aviana8.Data.AchievementEntity
import student.projects.aviana8.Data.AppStrings
import student.projects.aviana8.Data.BirdDatabase
import student.projects.aviana8.Data.SavedBird
import student.projects.aviana8.Data.TaxonomicBird
import student.projects.aviana8.Services.HotspotAPIClient
import student.projects.aviana8.Services.SettingsManager

class BirdsViewModel(
    birdDB: BirdDatabase,
    settingsManager: SettingsManager
) : ViewModel() {
    //val birdsList = mutableStateListOf<Bird>()

     val DB = Firebase.firestore
     val RTDB = Firebase.database
    val settingsMan = settingsManager

    val LDB = birdDB

    private val _birdsState = mutableStateOf<List< TaxonomicBird>>(emptyList())
    val birdsState = _birdsState

    private val _isLoading = mutableStateOf(false)
    val isLoading= _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error = _error
    private val _saveSuccess = mutableStateOf<String?>(null)
    val saveSuccess  = _saveSuccess

    private val _achievementEarned = mutableStateOf<AchievementEntity?>(null)
    val achievementEarned = _achievementEarned

    val savedBirds: Flow<List<SavedBird>> = birdDB.birdDao().getAllSavedBirds()
    val achievements: Flow<List<AchievementEntity>> =  birdDB.achievementDao().getAllAchievements()

    // Track first time save
    private var isFirstSave = true

    // Language state
    private val _currentLanguage = mutableStateOf(settingsManager.getLanguage())
    val currentLanguage = _currentLanguage


    // Profile methods
    fun getSavedBirdsCount(): Flow<Int> =LDB.birdDao().getAllSavedBirds().map { it.size }

    fun getEarnedAchievementsCount(): Flow<Int> = LDB.achievementDao().getAllAchievements()
        .map { achievements -> achievements.count { it.earned } }

    fun getAllSavedBirds(): Flow<List<SavedBird>> = LDB.birdDao().getAllSavedBirds()

    fun getAllAchievements(): Flow<List<AchievementEntity>> = LDB.achievementDao().getAllAchievements()

    // Get translated string
    fun getString(key: String): String {
        return AppStrings.getString(key, _currentLanguage.value)
    }

    // Update language
    fun updateLanguage(languageCode: String) {
        _currentLanguage.value = languageCode
        settingsMan.saveLanguage(languageCode)
    }

    init {
        loadBirds()
    }

    private fun initializeAchievements() {
        viewModelScope.launch {
            // Define initial achievements
            val initialAchievements = listOf(
                AchievementEntity(
                    id = "first_bird",
                    title = "First Sighting",
                    description = "Save your first bird",
                    requiredCount = 1
                ),
                AchievementEntity(
                    id = "bird_enthusiast",
                    title = "Bird Enthusiast",
                    description = "Save 3 different birds",
                    requiredCount = 3
                ),
                AchievementEntity(
                    id = "bird_watcher",
                    title = "Bird Watcher",
                    description = "Save 5 different birds",
                    requiredCount = 5
                ),
                AchievementEntity(
                    id = "bird_expert",
                    title = "Bird Expert",
                    description = "Save 10 different birds",
                    requiredCount = 10
                )
            )

            // Insert achievements if they don't exist
//            initialAchievements.forEach { achievement ->
//                if (birdDB..getAchievementById(achievement.id) == null) {
//                    achievementDao.insertAchievement(achievement)
//                }
//            }
        }
    }

    fun loadBirds() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val map: Map<String, String> = mapOf("X-eBirdApiToken" to "rhj2pqdjsgpu")
                //val response = connect.getDefaultTaxonomicBirds(map , locale)
                val birds = HotspotAPIClient.HotspotService.getDefaultTaxonomicBirds(
                    map,
                    "en"

                )

                if (birds.isSuccessful) {
                    val birds = birds.body()!!

                    _birdsState.value = birds.take(30)


                } else {

                    throw Exception(birds.message())

                }

               // _birdsState.value = birds.take(50) // Limit for demo
            } catch (e: Exception) {
                _error.value = "Failed to load birds: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun saveBirdToFirestore(bird: TaxonomicBird, firestore: FirebaseFirestore) {
        viewModelScope.launch {
            try {
                val user = Firebase.auth.currentUser
                user?.let {
                    val userId = it.uid

                    // Create a bird document with the speciesCode as document ID
                    val birdData = hashMapOf(
                        "speciesCode" to bird.speciesCode,
                        "commonName" to bird.comName,
                        "scientificName" to bird.sciName,
                        "familyCommonName" to bird.familyComName,
                        "familyScientificName" to bird.familySciName,
                        "order" to bird.order,
                        "category" to bird.category,
                        "savedAt" to FieldValue.serverTimestamp(),
                        "userId" to userId
                    )

                    DB.collection("saved_birds")
                        .document(bird.speciesCode)
                        .set(birdData)
                        .addOnSuccessListener {
                            _saveSuccess.value = "${bird.comName} saved successfully!"
                            // Clear success message after 3 seconds
                            viewModelScope.launch {
                                delay(3000)
                                _saveSuccess.value = null
                            }
                        }
                        .addOnFailureListener { e ->
                            _error.value = "Failed to save bird: ${e.message}"
                        }
                } ?: run {
                    _error.value = "Please log in to save birds"
                }
            } catch (e: Exception) {
                _error.value = "Failed to save bird1: ${e.message}"
            }
        }
    }

    fun saveBirdToLocal(bird: TaxonomicBird ,db: BirdDatabase) {
        viewModelScope.launch {
            try {
                // Convert API bird to SavedBird entity
                val savedBird = SavedBird(
                    speciesCode = bird.speciesCode,
                    commonName = bird.comName,
                    scientificName = bird.sciName,
                    familyCommonName = bird.familyComName,
                    familyScientificName = bird.familySciName,
                    order = bird.order,
                    category = bird.category
                )

                // Save to local database
                db.birdDao().insertBird(savedBird)

                // Check and update achievements
                checkAndUpdateAchievements(db)

                _saveSuccess.value = "${bird.comName} saved successfully!"

                // Show first time notification
                if (isFirstSave) {
                    showFirstSaveNotification()
                    isFirstSave = false
                }

                // Clear success message after 3 seconds
                viewModelScope.launch {
                    delay(3000)
                    _saveSuccess.value = null
                }
            } catch (e: Exception) {
                _error.value = "Failed to save bird: ${e.message}"
            }
        }
    }

    private fun checkAndUpdateAchievements(db: BirdDatabase) {
        viewModelScope.launch {
            try {
                // Get current saved birds count
                val savedCount = db.birdDao().getSavedBirdsCount()

                // Get all achievements
                val allAchievements = db.achievementDao().getAllAchievements().first()

                // Check which achievements should be earned
                val achievementsToUpdate = allAchievements.filter { achievement ->
                    savedCount >= achievement.requiredCount && !achievement.earned
                }

                // Update achievements in database
                achievementsToUpdate.forEach { achievement ->
                  db.achievementDao().updateAchievementEarned(
                        achievementId = achievement.id,
                        earned = true,
                        earnedAt = System.currentTimeMillis()
                    )

                    // Show notification for new achievement
                    showAchievementNotification(achievement)
                }
            } catch (e: Exception) {
                _error.value = "Error checking achievements: ${e.message}"
            }
        }
    }

    private fun showFirstSaveNotification() {
        _saveSuccess.value = "🎉 First bird saved! You're on your bird watching journey!"
    }

    private fun showAchievementNotification(achievement: AchievementEntity) {
        _achievementEarned.value = achievement
        _saveSuccess.value = "🏆 Achievement Unlocked: ${achievement.title} - ${achievement.description}"

        // Clear achievement after 5 seconds
        viewModelScope.launch {
            delay(5000)
            _achievementEarned.value = null
        }
    }

    fun clearMessages() {
        _error.value = null
        _saveSuccess.value = null
    }
}

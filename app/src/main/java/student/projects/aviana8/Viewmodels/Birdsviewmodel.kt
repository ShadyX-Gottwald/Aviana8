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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import student.projects.aviana8.Data.TaxonomicBird
import student.projects.aviana8.Services.HotspotAPIClient

class BirdsViewModel : ViewModel() {
    //val birdsList = mutableStateListOf<Bird>()

    private val db = FirebaseFirestore.getInstance()
     val RTDB = Firebase.database

    private val _birdsState = mutableStateOf<List< TaxonomicBird>>(emptyList())
    val birdsState = _birdsState

    private val _isLoading = mutableStateOf(false)
    val isLoading= _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error = _error

    init {
        loadBirds()
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

    fun saveBirdToFirebase(bird: TaxonomicBird, database: FirebaseDatabase) {
        viewModelScope.launch {
            try {
                val user = Firebase.auth.currentUser
                user?.let {
                    val userId = it.uid
                    val birdRef = database.getReference("users")
                        .child(userId).child("saved_birds")
                        .child(bird.speciesCode)
                    birdRef.setValue(bird)
                }
            } catch (e: Exception) {
                _error.value = "Failed to save bird: ${e.message}"
            }
        }
    }
}

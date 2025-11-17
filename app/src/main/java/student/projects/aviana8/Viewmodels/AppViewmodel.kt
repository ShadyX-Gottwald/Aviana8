package student.projects.aviana8.Viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import student.projects.aviana8.Screens.Screen

class AppViewModel : ViewModel() {
    // Shared app state
    var isLoggedIn by mutableStateOf(false)
    // var currentUser by mutableStateOf<User?>(null)

    // Navigation state
    var startDestination by mutableStateOf(Screen.Welcome.route)
    //var it = Screen.Welcome.route
}
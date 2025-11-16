package student.projects.aviana8.Viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import student.projects.aviana8.Screens.Screen


class AuthViewModel : ViewModel() {
    // Services implementation
   // private val _services = LoginRegisterImpl(auth)

    // Auth state - Updated to match our NetworkResponse<Any?> type
    private var _authState: MutableStateFlow<NetworkResponse<Any?>> =
        MutableStateFlow(NetworkResponse.Idle)
    val AuthState: StateFlow<NetworkResponse<Any?>> = _authState.asStateFlow()

    // Login User State - Simplified for our usage
    private var _loginUser: MutableStateFlow<LoginUserDTO> =
        MutableStateFlow(LoginUserDTO("", ""))
    val LoginUser = _loginUser.asStateFlow()

    // Convenience property for direct access
    var loginUser: LoginUserDTO
        get() = _loginUser.value
        private set(value) {
            _loginUser.value = value
        }

    // Register User State (for future use)
    /*private var _registerUser: MutableStateFlow<RegisterUserDTO> =
        MutableStateFlow(RegisterUserDTO())
    val RegisterUser = _registerUser.asStateFlow()

    var registerUser: RegisterUserDTO
        get() = _registerUser.value
        private set(value) {
            _registerUser.value = value
        }*/

    // Update login values - simplified version
    fun updateLoginValues(email: String, password: String): LoginUserDTO {
        val updated = LoginUserDTO(email, password)
        _loginUser.value = updated
        return updated
    }

    // Update email separately for real-time validation
    fun updateEmail(email: String) {
        loginUser = loginUser.copy(email = email)
    }

    // Update password separately for real-time validation
    fun updatePassword(password: String) {
        loginUser = loginUser.copy(password = password)
    }

    // Update register values (for Register screen)
  /*  fun updateRegisterValues(
        name: String = "",
        email: String = "",
        password: String = "",
        confirmpass: String = ""
    ): RegisterUserDTO {
        val updated = RegisterUserDTO(name, email, password, confirmpass)
        _registerUser.value = updated
        return updated
    }*/

    @SuppressLint("SuspiciousIndentation")
   // @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loginToFirebase(user: LoginUserDTO) = withContext(Dispatchers.IO) {
        try {
            _authState.emit(NetworkResponse.Loading)

         //   val result = _services.loginToFirebase(user)?.await(CancellationTokenSource())

         /*   if (result?.user != null) {
                _authState.emit(NetworkResponse.Success("Login successful"))
            } else {
                throw Exception("Login failed - no user returned")
            }*/

        } catch (e: Exception) {
            _authState.emit(
                NetworkResponse.Error(
                    message = e.message ?: "An unexpected error occurred"
                )
            )
        }
    }

    fun UpdateLoginValues(email: String ,
                          password: String): LoginUserDTO{

        val updated =  LoginUser.value.copy(email ,password)
        Log.i(Tag, updated.toString())
        Log.i(Tag, LoginUser.value.toString())
        loginUser = updated
        return updated

    }


    //@OptIn(ExperimentalCoroutinesApi::class)
   /* suspend fun registerToFirebase(user: RegisterUserDTO) = withContext(Dispatchers.IO) {
        try {
            _authState.emit(NetworkResponse.Loading)

            val res = _services.signUpToFirebase(
                user.Email,
                user.Password
            )?.await(CancellationTokenSource())

            if (res?.user?.email == user.Email) {
                _authState.emit(NetworkResponse.Success("Registration successful"))
            } else {
                throw Exception("Registration failed")
            }

        } catch (e: Exception) {
            _authState.emit(
                NetworkResponse.Error(
                    message = e.message ?: "Registration failed"
                )
            )
        }
    }*/

    // Reset auth state (useful when navigating away from login/register)
    fun resetAuthState() {
        viewModelScope.launch {
            _authState.emit(NetworkResponse.Idle)
        }
    }

    // Validate login form
    fun isLoginFormValid(): Boolean {
        return loginUser.email.isNotBlank() &&
                loginUser.password.isNotBlank() &&
                isValidEmail(loginUser.email)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        private const val Tag = "LoginRegisterViewModel"
    }
}

// NetworkResponse.kt
sealed class NetworkResponse<out T> {
    object Idle : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Error<out T>(val message: String, val data: T? = null) : NetworkResponse<T>()
}

// LoginUserDTO.kt
 data class LoginUserDTO(
    val email: String,
    val password: String
)

class HomeViewModel : ViewModel() {
    val homeData = mutableStateOf("Home Data")
}

class BirdsViewModel : ViewModel() {
    //val birdsList = mutableStateListOf<Bird>()
}

class ProfileViewModel : ViewModel() {
   // val userProfile = mutableStateOf<UserProfile?>(null)
}

class AppViewModel : ViewModel() {
    // Shared app state
    var isLoggedIn by mutableStateOf(false)
   // var currentUser by mutableStateOf<User?>(null)

    // Navigation state
    var startDestination by mutableStateOf(Screen.Welcome.route)
    //var it = Screen.Welcome.route
}


package student.projects.aviana8

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import student.projects.aviana8.Data.BirdDatabase
import student.projects.aviana8.Screens.LoginPage

import student.projects.aviana8.Screens.MainBottomNavigation

import student.projects.aviana8.Screens.Screen
import student.projects.aviana8.Screens.WelcomeScreen
import student.projects.aviana8.Services.HotspotAPIClient
import student.projects.aviana8.Services.HotspotRepository
import student.projects.aviana8.Services.LocationService
import student.projects.aviana8.Services.NetworkMonitor
import student.projects.aviana8.Viewmodels.AppViewModel

import student.projects.aviana8.Viewmodels.AuthViewModel
import student.projects.aviana8.Viewmodels.BirdsViewModel
import student.projects.aviana8.Viewmodels.HomeViewModel
import student.projects.aviana8.Viewmodels.ProfileViewModel
import student.projects.aviana8.ui.theme.Aviana8Theme

class MainActivity : ComponentActivity() {

    // Create ViewModels that will be shared across the app

    // Create ViewModels that will be shared across the app
    private lateinit var appViewModel: AppViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var birdsViewModel: BirdsViewModel
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize all components
        val birdDatabase = BirdDatabase.getInstance(this)
        val locationService = LocationService(this)
        val networkMonitor = NetworkMonitor(this)

        val hotspotRepository = HotspotRepository(
            hotspotService = HotspotAPIClient.HotspotService,
            database = birdDatabase,
            networkMonitor = networkMonitor,
            context = this
        )
       // enableEdgeToEdge()
        // Initialize ViewModels
        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        //homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        birdsViewModel = ViewModelProvider(this)[BirdsViewModel::class.java]
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val homeViewModel by lazy{ HomeViewModel(
            hotspotRepository = hotspotRepository ,locationService) }
        setContent {
            Aviana8Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        appViewModel= appViewModel,
                        authViewModel = authViewModel,
                        homeViewModel = homeViewModel,
                        birdsViewModel = birdsViewModel,
                        profileViewModel = profileViewModel
                    )
                }
            }
        }
    }
}

// Main Navigation Composable
@Composable
fun AppNavigation(
appViewModel: AppViewModel,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    birdsViewModel: BirdsViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = appViewModel.startDestination
    ) {
        // Auth Flow
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                appViewModel = appViewModel,
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                context = context
            )
        }

        composable(Screen.Login.route) {
            LoginPage(
                viewModel = authViewModel,
                navToRegisterPage = { navController.navigate(Screen.Register.route) },
                loginButtonClick = { loginuser ->
                    //authViewModel.login(loginuser)

                },
                navToHomePage = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    appViewModel.isLoggedIn = true
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        // Main App Flow with Bottom Navigation
        composable(Screen.Home.route) {
            MainBottomNavigation(
                homeViewModel = homeViewModel,
                birdsViewModel = birdsViewModel,
                profileViewModel = profileViewModel
            )
        }
    }
}

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    TODO("Not yet implemented")
}


@Composable
fun BirdWatchingAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        content = content
    )
}
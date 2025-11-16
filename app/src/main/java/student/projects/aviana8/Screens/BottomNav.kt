package student.projects.aviana8.Screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import student.projects.aviana8.Viewmodels.BirdsViewModel
import student.projects.aviana8.Viewmodels.HomeViewModel
import student.projects.aviana8.Viewmodels.ProfileViewModel

@Composable
fun MainBottomNavigation(
    homeViewModel: HomeViewModel,
    birdsViewModel: BirdsViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    val currentRoute = currentRoute(navController)

                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = "Birds") },
                        label = { Text("Birds") },
                        selected = currentRoute == Screen.Birds.route,
                        onClick = {
                            navController.navigate(Screen.Birds.route) {
                                popUpTo(Screen.Birds.route) { inclusive = true }
                            }
                        }
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        selected = currentRoute == Screen.ProfileSettings.route,
                        onClick = {
                            navController.navigate(Screen.ProfileSettings.route) {
                                popUpTo(Screen.ProfileSettings.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    homeViewModel = homeViewModel,
                    profileViewModel = profileViewModel
                )
            }
            composable(Screen.Birds.route) {
                BirdsScreen(birdsViewModel = birdsViewModel)
            }
            composable(Screen.ProfileSettings.route) {
                ProfileSettingsScreen(profileViewModel = profileViewModel)
            }
        }
    }
}

// Helper function to get current route
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
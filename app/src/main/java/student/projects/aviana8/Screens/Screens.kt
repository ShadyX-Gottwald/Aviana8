package student.projects.aviana8.Screens

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")

    // Bottom Nav Screens
    object Home : Screen("home")
    object Birds : Screen("birds")
    object ProfileSettings : Screen("profile")
    object Settings : Screen("profile")

}
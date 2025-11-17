package student.projects.aviana8.Screens

import android.Manifest
import android.R
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import student.projects.aviana8.Services.PermissionsManager
import student.projects.aviana8.Viewmodels.AppViewModel
import student.projects.aviana8.Viewmodels.AuthViewModel
import student.projects.aviana8.Viewmodels.BirdsViewModel
import student.projects.aviana8.Viewmodels.HomeViewModel
import student.projects.aviana8.Viewmodels.ProfileViewModel
import student.projects.aviana8.ui.theme.Brown
import student.projects.aviana8.ui.theme.GreenOutline
import student.projects.aviana8.ui.theme.Peach
import student.projects.aviana8.ui.theme.TextBrown

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Scaffold
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import student.projects.aviana8.ui.theme.WhiteNew

@Composable
fun WelcomeScreen(
    appViewModel: AppViewModel,
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit ,
    context: Context
) {
    val permissionsManager = remember { PermissionsManager() }

    var context = LocalContext.current
    // Permission states
    var hasLocationPermission by remember {
        mutableStateOf(permissionsManager.hasLocationPermission(context))
    }
    var hasNotificationPermission by remember {
        mutableStateOf(permissionsManager.hasNotificationPermission(context))
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    // Check if all required permissions are granted
    val allPermissionsGranted = hasLocationPermission && hasNotificationPermission

    WelcomeContent(
        hasLocationPermission = hasLocationPermission,
        hasNotificationPermission = hasNotificationPermission,
        allPermissionsGranted = allPermissionsGranted,
        onRequestLocationPermission = {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        },
        onRequestNotificationPermission = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToRegister = onNavigateToRegister
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeContent(
    hasLocationPermission: Boolean,
    hasNotificationPermission: Boolean,
    allPermissionsGranted: Boolean,
    onRequestLocationPermission: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Scaffold(
        containerColor = WhiteNew
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Section with Logo
            HeaderSection()

            // Permissions Section
            PermissionsSection(
                hasLocationPermission = hasLocationPermission,
                hasNotificationPermission = hasNotificationPermission,
                onRequestLocationPermission = onRequestLocationPermission,
                onRequestNotificationPermission = onRequestNotificationPermission
            )

            // Action Section (only show when permissions are granted)
            if (allPermissionsGranted) {
                ActionSection(
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToRegister = onNavigateToRegister
                )
            } else {
                // Show permission prompt
                PermissionPromptSection()
            }

            // Footer
            FooterSection()
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo/Bird Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Peach, CircleShape)
                .border(2.dp, GreenOutline, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_up_float),
                contentDescription = "Aviana Logo",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
                , contentScale = ContentScale.FillWidth

            )


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BirdWatcher",
                style = MaterialTheme.typography.displaySmall,
                color = TextBrown,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Discover the fascinating world of birds around you",
                style = MaterialTheme.typography.bodyLarge,
                color = TextBrown.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }



}

@Composable
fun PermissionsSection(
    hasLocationPermission: Boolean,
    hasNotificationPermission: Boolean,
    onRequestLocationPermission: () -> Unit,
    onRequestNotificationPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enable Features for Better Experience",
            style = MaterialTheme.typography.headlineSmall,
            color = TextBrown,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Location Permission Card
        PermissionCard(
            icon = Icons.Default.LocationOn,
            title = "Location Access",
            description = "Track birds in your area and discover local species",
            isGranted = hasLocationPermission,
            onRequestPermission = onRequestLocationPermission,
            grantedText = "Location access enabled",
            buttonText = "Allow Location"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Notification Permission Card
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionCard(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                description = "Get alerts about rare bird sightings and birding events",
                isGranted = hasNotificationPermission,
                onRequestPermission = onRequestNotificationPermission,
                grantedText = "Notifications enabled",
                buttonText = "Allow Notifications"
            )
        } else {
            PermissionCard(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                description = "Get alerts about rare bird sightings and birding events",
                isGranted = hasNotificationPermission,
                onRequestPermission = onRequestNotificationPermission,
                grantedText = "Notifications enabled",
                buttonText = "Allow Notifications"
            )
        }
    }
}







@Composable
fun PermissionCard(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    onRequestPermission: () -> Unit,
    grantedText: String,
    buttonText: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) GreenOutline.copy(alpha = 0.1f) else WhiteNew
        ),
        border = BorderStroke(1.dp, GreenOutline.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isGranted) GreenOutline else Peach,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextBrown,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isGranted) grantedText else description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBrown.copy(alpha = 0.7f)
                )

                if (!isGranted) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onRequestPermission,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Peach,
                            contentColor = Brown
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = buttonText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (isGranted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Granted",
                    tint = GreenOutline,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PermissionPromptSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please enable all permissions to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = TextBrown.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionSection(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Success indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "All set! Ready to explore",
                style = MaterialTheme.typography.bodyLarge,
                color = GreenOutline,
                fontWeight = FontWeight.Medium
            )
        }

        // Login Button
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Peach,
                contentColor = Brown
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Register Button
        OutlinedButton(
            onClick = onNavigateToRegister,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Peach
            ),
            border = BorderStroke(2.dp, Peach)
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Begin your bird watching journey today",
            style = MaterialTheme.typography.bodySmall,
            color = TextBrown.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}

// LoginScreen.kt


// HomeScreen.kt


// BirdsScreen.kt
@Composable
fun BirdsScreen(birdsViewModel: BirdsViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Birds Catalog", style = MaterialTheme.typography.headlineMedium)
        // Birds list content
    }
}

// ProfileSettingsScreen.kt
@Composable
fun ProfileSettingsScreen(profileViewModel: ProfileViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Profile & Settings", style = MaterialTheme.typography.headlineMedium)
        // Profile and settings content
    }
}

@Composable
fun SettingsScreen(profileViewModel: ProfileViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Profile & Settings", style = MaterialTheme.typography.headlineMedium)
        // Profile and settings content
    }
}
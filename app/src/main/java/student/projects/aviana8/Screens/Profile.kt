package student.projects.aviana8.Screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import student.projects.aviana8.Data.AchievementEntity
import student.projects.aviana8.Data.AppLanguage
import student.projects.aviana8.Data.SavedBird
import student.projects.aviana8.Data.TaxonomicBird
import student.projects.aviana8.Viewmodels.BirdsViewModel
import student.projects.aviana8.Viewmodels.ProfileViewModel
import student.projects.aviana8.ui.theme.Brown
import student.projects.aviana8.ui.theme.DarkPurpleBackGround
import student.projects.aviana8.ui.theme.GreenOutline
import student.projects.aviana8.ui.theme.HomePeach
import student.projects.aviana8.ui.theme.Peach
import student.projects.aviana8.ui.theme.TextBrown
import student.projects.aviana8.ui.theme.WhiteNew
import student.projects.aviana8.ui.theme.WhiteishBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ProfileScreen(
    viewModel: BirdsViewModel,
    onBackClick: () -> Unit = {}
) {
    val savedBirds by viewModel.getAllSavedBirds().collectAsState(initial = emptyList())
    val achievements by viewModel.getAllAchievements().collectAsState(initial = emptyList())
    val savedBirdsCount by viewModel.getSavedBirdsCount().collectAsState(initial = 0)
    val earnedAchievementsCount by viewModel.getEarnedAchievementsCount().collectAsState(initial = 0)

    val currentLanguage = viewModel.currentLanguage.value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteishBg)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkPurpleBackGround)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = HomePeach
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = viewModel.getString("profile_settings"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = HomePeach,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column {
            // Test Notifications Section - Put this FIRST so it's immediately visible

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Peach),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = DarkPurpleBackGround,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "FCM Notifications Ready!",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkPurpleBackGround,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Your device is subscribed to receive test notifications",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkPurpleBackGround,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Button(
                        onClick = {
                            // Manually trigger topic subscription
                            FirebaseMessaging.getInstance().subscribeToTopic("test")
                                .addOnCompleteListener { task ->
                                    val message = if (task.isSuccessful) {
                                        "Subscribed to test topic!"
                                    } else {
                                        "Subscription failed"
                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkPurpleBackGround),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Subscribe to Test Topic",
                            color = WhiteishBg
                        )
                    }


                }
            }
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // User Stats Section
            item {
                Text(
                    text = viewModel.getString("my_stats"),
                    style = MaterialTheme.typography.titleLarge,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    // Saved Birds Card
                    StatCard(
                        title = viewModel.getString("saved_birds"),
                        count = savedBirdsCount,
                        icon = Icons.Default.Favorite,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Achievements Card
                    StatCard(
                        title = viewModel.getString("achievements"),
                        count = earnedAchievementsCount,
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Language Settings Section
            item {
                Text(
                    text = viewModel.getString("language_settings"),
                    style = MaterialTheme.typography.titleLarge,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = WhiteNew),
                    elevation = CardDefaults.cardElevation(2.dp),
                    border = BorderStroke(1.dp, GreenOutline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        AppLanguage.allLanguages.forEach { language ->
                            LanguageOption(
                                language = language,
                                isSelected = currentLanguage == language.code,
                                onLanguageSelected = {
                                    viewModel.updateLanguage(language.code)
                                }
                            )
                            if (language != AppLanguage.allLanguages.last()) {
                                Divider(
                                    color = GreenOutline.copy(alpha = 0.3f),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Saved Birds Section
            item {
                Text(
                    text = viewModel.getString("saved_birds"),
                    style = MaterialTheme.typography.titleLarge,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (savedBirds.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Default.FavoriteBorder,
                        title = viewModel.getString("no_birds_saved"),
                        description = viewModel.getString("start_saving"),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            } else {
                items(savedBirds) { bird ->
                    SavedBirdItem(
                        bird = bird,
                        onDeleteClick = {
                          //  viewModel.deleteSavedBird(bird)
                                        },
                        savedOnText = viewModel.getString("saved_on"),
                        deleteText = viewModel.getString("delete"),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Achievements Section
            item {
                Text(
                    text = viewModel.getString("achievements"),
                    style = MaterialTheme.typography.titleLarge,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
            }

            if (achievements.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Default.StarOutline,
                        title = viewModel.getString("no_achievements"),
                        description = viewModel.getString("save_more_birds"),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            } else {
                items(achievements) { achievement ->
                    AchievementItem(
                        achievement = achievement,
                        earnedOnText = viewModel.getString("earned_on"),
                        saveToUnlockText = viewModel.getString("save_to_unlock"),
                        birdsSavedText = viewModel.getString("birds_saved"),
                        viewModel = viewModel,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Bottom padding
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }


    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = WhiteNew),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Brown,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextBrown
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextBrown.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun SavedBirdItem(
    bird: SavedBird,
    onDeleteClick: () -> Unit,
    savedOnText: String,
    deleteText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = WhiteNew),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, GreenOutline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bird.commonName,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextBrown,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = bird.scientificName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBrown.copy(alpha = 0.7f),
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "$savedOnText ${formatDate(bird.savedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextBrown.copy(alpha = 0.5f)
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = deleteText,
                    tint = Brown
                )
            }
        }
    }
}

@Composable
fun AchievementItem(
    achievement: AchievementEntity,
    earnedOnText: String,
    saveToUnlockText: String,
    birdsSavedText: String,
    viewModel: BirdsViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.earned) Peach else WhiteNew
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(
            1.dp,
            if (achievement.earned) GreenOutline else GreenOutline.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (achievement.earned) Icons.Default.Star else Icons.Default.StarOutline,
                contentDescription = "Achievement",
                tint = if (achievement.earned) DarkPurpleBackGround else TextBrown.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = viewModel.getString(achievement.id), // Use achievement ID as key
                    style = MaterialTheme.typography.titleMedium,
                    color = if (achievement.earned) DarkPurpleBackGround else TextBrown,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = getAchievementDescription(achievement, viewModel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (achievement.earned) DarkPurpleBackGround.copy(alpha = 0.8f)
                    else TextBrown.copy(alpha = 0.7f)
                )
                if (achievement.earned && achievement.earnedAt != null) {
                    Text(
                        text = "$earnedOnText ${formatDate(achievement.earnedAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (achievement.earned) DarkPurpleBackGround.copy(alpha = 0.6f)
                        else TextBrown.copy(alpha = 0.5f)
                    )
                } else {
                    Text(
                        text = "$saveToUnlockText ${achievement.requiredCount} $birdsSavedText",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextBrown.copy(alpha = 0.5f)
                    )
                }
            }

            if (achievement.earned) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Achieved",
                    tint = GreenOutline,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}



@Composable
fun BirdCard(
    bird: TaxonomicBird,
    onSaveClick: () -> Unit,
    isSaving: Boolean = false,
    isAlreadySaved: Boolean = false,
    saveButtonText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteNew),
        border = BorderStroke(1.dp, GreenOutline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Placeholder Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Brown, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bird Image",
                    color = WhiteishBg,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bird Information
            Text(
                text = bird.comName,
                style = MaterialTheme.typography.titleMedium,
                color = TextBrown,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = bird.sciName,
                style = MaterialTheme.typography.bodyMedium,
                color = TextBrown.copy(alpha = 0.8f),
                fontStyle = FontStyle.Italic
            )

            bird.familyComName?.let { family ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Family: $family",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextBrown.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Save Button with state
            Button(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAlreadySaved) GreenOutline else Peach
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving && !isAlreadySaved
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = DarkPurpleBackGround,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = saveButtonText,
                        color = DarkPurpleBackGround,
                        fontWeight = FontWeight.Medium
                    )
                } else if (isAlreadySaved) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Saved",
                        tint = WhiteishBg
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = saveButtonText,
                        color = WhiteishBg,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = saveButtonText,
                        color = DarkPurpleBackGround,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageOption(
    language: AppLanguage,
    isSelected: Boolean,
    onLanguageSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onLanguageSelected() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = language.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = TextBrown,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = when (language) {
                    is AppLanguage.English -> "English"
                    is AppLanguage.Afrikaans -> "Afrikaans"
                    is AppLanguage.Xhosa -> "isiXhosa"
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextBrown.copy(alpha = 0.7f)
            )
        }

        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                tint = GreenOutline
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Peach),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = DarkPurpleBackGround,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = DarkPurpleBackGround,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkPurpleBackGround
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

private fun getAchievementDescription(achievement: AchievementEntity, viewModel: BirdsViewModel): String {
    return when (achievement.id) {
        "first_bird" -> viewModel.getString("save_first_bird")
        "bird_enthusiast" -> viewModel.getString("save_3_birds")
        "bird_watcher" -> viewModel.getString("save_5_birds")
        "bird_expert" -> viewModel.getString("save_10_birds")
        else -> achievement.description
    }
}
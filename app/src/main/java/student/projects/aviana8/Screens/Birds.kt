package student.projects.aviana8.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import student.projects.aviana8.Data.TaxonomicBird
import student.projects.aviana8.Viewmodels.BirdsViewModel
import student.projects.aviana8.ui.theme.Brown
import student.projects.aviana8.ui.theme.DarkPurpleBackGround
import student.projects.aviana8.ui.theme.GreenOutline
import student.projects.aviana8.ui.theme.HomePeach
import student.projects.aviana8.ui.theme.Peach
import student.projects.aviana8.ui.theme.TextBrown
import student.projects.aviana8.ui.theme.WhiteNew
import student.projects.aviana8.ui.theme.WhiteishBg

@SuppressLint("UnrememberedMutableState")
@Composable
fun BirdsScreen(birdsViewModel: BirdsViewModel) {
    //val birds by birdsViewModel.birdsState
    //val isLoading by birdsViewModel.isLoading
   // val error by birdsViewModel.error

    val birds by birdsViewModel.birdsState
    val isLoading by birdsViewModel.isLoading
    val error by birdsViewModel.error
    val saveSuccess by birdsViewModel.saveSuccess

    // Track saving state
    var isSaving by remember { mutableStateOf(false) }


    val achievementEarned by birdsViewModel.achievementEarned

    // Collect flows from database
    val savedBirds by birdsViewModel.savedBirds.collectAsState(initial = emptyList())
    val achievements by birdsViewModel.achievements.collectAsState(initial = emptyList())



    // Calculate current progress
    val currentProgress by derivedStateOf {
        savedBirds.size
    }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aviana",
                    style = MaterialTheme.typography.headlineMedium,
                    color = HomePeach,
                    fontWeight = FontWeight.Bold
                )

                // Achievement counter
                Text(
                    text = "$currentProgress/10",
                    style = MaterialTheme.typography.titleMedium,
                    color = HomePeach,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Achievement Earned Banner
        achievementEarned?.let { achievement ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Peach.copy(alpha = 0.9f))
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏆 Achievement Unlocked!",
                        color = DarkPurpleBackGround,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = achievement.title,
                        color = DarkPurpleBackGround,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = achievement.description,
                        color = DarkPurpleBackGround,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Success Message
        saveSuccess?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenOutline.copy(alpha = 0.8f))
                    .padding(8.dp)
            ) {
                Text(
                    text = message,
                    color = WhiteishBg,
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Error Message
        error?.let { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brown.copy(alpha = 0.8f))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMessage,
                        color = WhiteishBg,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { birdsViewModel.clearMessages() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = WhiteishBg
                        )
                    }
                }
            }
        }

        // Progress Bar for Achievements
        if (achievements.isNotEmpty()) {
            val earnedAchievements = achievements.filter { it.earned }
            val maxEarned = earnedAchievements.maxByOrNull { it.requiredCount }?.requiredCount ?: 0

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Bird Watching Progress" ,
                    color = TextBrown,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { currentProgress / 10f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Peach,
                    trackColor = GreenOutline.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$currentProgress/10 birds saved",
                    color = TextBrown,
                    style = MaterialTheme.typography.bodySmall
                )

                // Achievement badges
                if (earnedAchievements.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${birdsViewModel.getString("achievements")}:",
                        color = TextBrown,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow {
                        items(earnedAchievements) { achievement ->
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(Peach, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = achievement.title,
                                    color = DarkPurpleBackGround,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Peach)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = birdsViewModel.getString("loading_birds"),
                        color = TextBrown
                    )
                }
            }
        }

        if (!isLoading && error == null && birds.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${birdsViewModel.getString("no_birds_found")}",
                        color = TextBrown,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { birdsViewModel.loadBirds() },
                        colors = ButtonDefaults.buttonColors(containerColor = Peach)
                    ) {
                        Text("${birdsViewModel.getString("retry")}", color = DarkPurpleBackGround)
                    }
                }
            }
        }

        if (!isLoading && error == null && birds.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(birds) { bird ->
                    BirdCard(
                        bird = bird,
                        onSaveClick = {
                            isSaving = true
                            birdsViewModel.saveBirdToLocal(bird ,birdsViewModel.LDB)
                            // Reset saving state after a short delay
                           birdsViewModel.viewModelScope.launch {
                                delay(2000)
                                isSaving = false
                            }
                        },
                        isSaving = isSaving,
                        isAlreadySaved = savedBirds.any { it.speciesCode == bird.speciesCode } ,
                        birdsViewModel = birdsViewModel
                    )
                }
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
    modifier: Modifier = Modifier ,
    birdsViewModel: BirdsViewModel
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
                    text = "${birdsViewModel.getString("Family")}: $family",
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
                        text = "${birdsViewModel.getString("saving")}",
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
                        text = "${birdsViewModel.getString("already_saved")}",
                        color = WhiteishBg,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "${birdsViewModel.getString("save_to_my_birds")}",
                        color = DarkPurpleBackGround,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
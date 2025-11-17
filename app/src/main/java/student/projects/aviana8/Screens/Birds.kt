package student.projects.aviana8.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

@Composable
fun BirdsScreen(birdsViewModel: BirdsViewModel) {
    val birds by birdsViewModel.birdsState
    val isLoading by birdsViewModel.isLoading
    val error by birdsViewModel.error

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
                .padding(16.dp)
        ) {
            Text(
                text = "Bird Watch",
                style = MaterialTheme.typography.headlineMedium,
                color = HomePeach,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Peach)
            }
        }

        error?.let { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = errorMessage,
                        color = Brown,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { birdsViewModel.loadBirds() },
                        colors = ButtonDefaults.buttonColors(containerColor = Peach)
                    ) {
                        Text("Retry", color = DarkPurpleBackGround)
                    }
                }
            }
        }

        if (!isLoading && error == null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(birds) { bird ->
                    BirdCard(
                        bird = bird,
                        onSaveClick = {
                            birdsViewModel.saveBirdToFirebase(bird, birdsViewModel.RTDB)
                        }
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

            // Save Button
            Button(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(containerColor = Peach),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Save to My Birds",
                    color = DarkPurpleBackGround,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
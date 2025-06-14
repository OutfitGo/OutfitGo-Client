package com.outfitgo.store.presentation.aboutus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outfitgo.store.R
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    team: List<TeamMember>,
    modifier: Modifier = Modifier,
    onLinkedInClicked: (String) -> Unit,
    onGithubClicked: (String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Us") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background))
        },
        modifier = modifier
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(team) { member ->
                TeamMemberCard(
                    teamMember = member,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onGithubClicked = onGithubClicked,
                    onLinkedInClicked = onLinkedInClicked
                )
            }
        }
    }


}

@Composable
fun TeamMemberCard(
    modifier: Modifier = Modifier,
    teamMember: TeamMember,
    onLinkedInClicked: (String) -> Unit,
    onGithubClicked: (String) -> Unit
) {
    Box(
        modifier
            .height(300.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(teamMember.imgRes),
            contentDescription = teamMember.name,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f),
                        ),
                        startY = 0.0f,
                        endY = 300.0f
                    )
                )
        )

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            Column {
                Text(
                    teamMember.name,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(R.drawable.linkedin_logo),
                        contentDescription = "linkedin",
                        Modifier
                            .size(24.dp)
                            .clickable {
                                onLinkedInClicked(teamMember.linkedIn)
                            }
                    )
                    Image(
                        painter = painterResource(R.drawable.github),
                        contentDescription = "linkedin",
                        Modifier
                            .size(24.dp)
                            .clickable {
                                onGithubClicked(teamMember.github)
                            }
                    )
                }
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun AboutUsScreenPreview() {
    OutfitGoTheme {
        AboutUsScreen(
            team = listOf(),
            modifier = Modifier.fillMaxSize(),
            onLinkedInClicked = {},
            onGithubClicked = {},
            onNavigateUp = {}
        )
    }
}

data class TeamMember(
    val name: String,
    val imgRes: Int,
    val linkedIn: String,
    val github: String,
)
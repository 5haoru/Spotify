package com.example.myspotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.DataManager
import com.example.myspotify.ui.home.HomeTabView
import com.example.myspotify.ui.library.YourLibraryTabView
import com.example.myspotify.ui.premium.PremiumTabView
import com.example.myspotify.ui.search.SearchTabView
import com.example.myspotify.ui.theme.MySpotifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MySpotifyTheme {
                MainScreen(dataManager = DataManager(this))
            }
        }
    }
}

@Composable
fun MainScreen(dataManager: DataManager) {
    var selectedTab by remember { mutableStateOf(0) }
    val songs = remember { dataManager.getSongs() }
    val artists = remember { dataManager.getArtists() }
    val albums = remember { dataManager.getAlbums() }

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer()
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> HomeTabView(songs = songs, albums = albums)
                1 -> SearchTabView()
                2 -> YourLibraryTabView(artists = artists)
                3 -> PremiumTabView()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 12.sp) },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search", fontSize = 12.sp) },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Your Library") },
            label = { Text("Your Library", fontSize = 12.sp) },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Premium") },
            label = { Text("Premium", fontSize = 12.sp) },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun MiniPlayer() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = Color(0xFF8B0000)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF5C0000))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "IRIS OUT",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Kenshi Yonezu",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Devices",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Like",
                        tint = Color(0xFF1DB954)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

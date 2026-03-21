package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.model.Artist

/**
 * YourLibraryTab View - 音乐库页面视图
 */
@Composable
fun YourLibraryTabView(
    artists: List<Artist>
) {
    var selectedTab by remember { mutableStateOf("Playlists") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF5722)),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Your Library",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }

        // Playlists/Artists 标签
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTab == "Playlists",
                onClick = { selectedTab = "Playlists" },
                label = { Text("Playlists") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF1DB954),
                    selectedLabelColor = Color.Black,
                    containerColor = Color(0xFF282828),
                    labelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedTab == "Artists",
                onClick = { selectedTab = "Artists" },
                label = { Text("Artists") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF1DB954),
                    selectedLabelColor = Color.Black,
                    containerColor = Color(0xFF282828),
                    labelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            // Recents 排序
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Sort",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recents",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Grid View",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Import music 提示
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Import",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Import your music",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "from other apps",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Go",
                        tint = Color.White
                    )
                }
            }

            // Liked Songs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF6B4FBB),
                                        Color(0xFF1DB954)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Liked",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Liked Songs",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Pinned",
                                tint = Color(0xFF1DB954),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Playlist • 1 song",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // 艺术家列表
            items(artists) { artist ->
                ArtistItem(artist = artist)
            }

            // Add artists 按钮
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282828)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Add artists",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 底部间距
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * 艺术家项组件
 */
@Composable
fun ArtistItem(artist: Artist) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFF282828))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = artist.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Artist",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

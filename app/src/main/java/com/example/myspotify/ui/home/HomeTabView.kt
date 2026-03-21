package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.model.Album
import com.example.myspotify.model.Song

/**
 * HomeTab View - 首页视图
 */
@Composable
fun HomeTabView(
    songs: List<Song>,
    albums: List<Album>,
    onSongClick: (Song) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Music", "Podcasts", "Audiobooks")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部用户头像和分类标签
        item {
            Column {
                // 用户头像
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF5722)),
                        tint = Color.White
                    )
                }

                // 分类标签
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1DB954),
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF282828),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // "Start listening" 部分
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Jump into a session based on your tastes",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Start listening",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // 歌曲列表
        items(songs.take(3)) { song ->
            SongItem(song = song, onClick = { onSongClick(song) })
        }

        // "To get you started" 部分
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "To get you started",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // 推荐卡片
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(albums.take(3)) { album ->
                    RecommendCard(
                        title = "${album.artistName} Mix",
                        subtitle = album.artistName
                    )
                }
            }
        }

        // 底部间距
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * 歌曲项组件
 */
@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF282828))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 歌曲信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = song.artistName,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        // 更多按钮
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.Gray
            )
        }
    }
}

/**
 * 推荐卡片组件
 */
@Composable
fun RecommendCard(title: String, subtitle: String) {
    Column(
        modifier = Modifier.width(160.dp)
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF282828))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = subtitle,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

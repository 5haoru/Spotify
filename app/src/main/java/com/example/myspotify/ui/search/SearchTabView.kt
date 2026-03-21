package com.example.myspotify.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * SearchTab View - 搜索页面视图
 */
@Composable
fun SearchTabView() {
    var searchText by remember { mutableStateOf("") }

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
                    text = "Search",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Camera",
                    tint = Color.White
                )
            }
        }

        // 搜索框
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text("What do you want to listen to?", color = Color.Gray)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            // "Start browsing" 标题
            item {
                Text(
                    text = "Start browsing",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 分类网格
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        listOf(
                            "Music" to Color(0xFFE91E63),
                            "Podcasts" to Color(0xFF009688),
                            "Audiobooks" to Color(0xFF3F51B5),
                            "Live Events" to Color(0xFF9C27B0)
                        )
                    ) { (title, color) ->
                        CategoryCard(title = title, backgroundColor = color)
                    }
                }
            }

            // "Discover something new" 标题
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Discover something new",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 发现卡片
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DiscoverCard(
                        title = "Music for you",
                        modifier = Modifier.weight(1f)
                    )
                    DiscoverCard(
                        title = "#japanese pop rock",
                        modifier = Modifier.weight(1f)
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
 * 分类卡片组件
 */
@Composable
fun CategoryCard(title: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * 发现卡片组件
 */
@Composable
fun DiscoverCard(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF282828)),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

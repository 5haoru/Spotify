package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * CreditsTab View - 歌曲制作人员名单页面
 */
@Composable
fun CreditsTabView(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Credits",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 可滚动内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 歌曲信息
            Text(
                text = "IRIS OUT",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Kenshi Yonezu",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Credits 列表
            CreditsItem(name = "Kenshi Yonezu", role = "Performed by")
            CreditsItem(name = "Kenshi Yonezu", role = "Written by")
            CreditsItem(name = "Kenshi Yonezu, Taro Yamada", role = "Produced by")
            CreditsItem(name = "Jiro Suzuki", role = "Mixing Engineer")
            CreditsItem(name = "Saburo Tanaka", role = "Mastering Engineer")
            CreditsItem(name = "Kenshi Yonezu", role = "Guitar")
            CreditsItem(name = "Shiro Sato", role = "Bass")
            CreditsItem(name = "Goro Watanabe", role = "Drums")
            CreditsItem(name = "Kenshi Yonezu", role = "Vocals")
            CreditsItem(name = "Sony Music Labels", role = "Label")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Credits 条目组件
 */
@Composable
private fun CreditsItem(name: String, role: String) {
    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = role,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.ui.common.AssetImage

/**
 * AboutTheArtistTab View - 艺术家介绍页面
 */
@Composable
fun AboutTheArtistTabView(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "About the artist",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 可滚动内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 大尺寸艺术家图片
            AssetImage(
                assetPath = "avatar/7.png",
                contentDescription = "Kenshi Yonezu",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 艺术家名
                Text(
                    text = "Kenshi Yonezu",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 月听众数
                Text(
                    text = "5.2M monthly listeners",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Follow 按钮
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                ) {
                    Text(
                        text = "Follow",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 艺术家介绍
                Text(
                    text = "Kenshi Yonezu is a Japanese musician, singer-songwriter, record producer, and illustrator born on March 10, 1991 in Tokushima Prefecture, Japan. He first gained attention in the early 2010s as a Vocaloid producer under the name \"Hachi\", uploading original compositions to Niconico that quickly amassed millions of views.\n\nIn 2012, Yonezu transitioned to performing his own vocals and released his debut album \"diorama\" independently. His unique blend of pop, rock, and electronic elements, combined with deeply introspective lyrics, set him apart from his contemporaries. His major-label debut came in 2014 with the album \"YANKEE\", which debuted at number one on the Oricon charts.\n\nHis 2018 single \"Lemon\" became one of the best-selling digital singles in Japanese music history, surpassing 3 million downloads and accumulating over 800 million views on YouTube. The song was written as a tribute to his late grandfather and resonated deeply with listeners across Asia and beyond.\n\nYonezu continues to push creative boundaries, collaborating with international artists and composing music for major anime series and films. His artistic vision extends beyond music into visual art and animation, making him one of the most versatile creative forces in contemporary Japanese culture.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // ===== Posted by the artist =====
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Posted by the artist",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 帖子卡片
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF282828))
                        .padding(16.dp)
                ) {
                    // 艺术家头像 + 名字 + 日期
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssetImage(
                            assetPath = "avatar/7.png",
                            contentDescription = "Kenshi Yonezu",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Kenshi Yonezu",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Dec 15, 2024",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 帖子内容
                    Text(
                        text = "My new album \"LOST CORNER\" is out now! This album is very special to me — it represents a journey through landscapes both real and imagined. I hope you enjoy listening to it as much as I enjoyed creating it. Thank you for your continued support. 🎵",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 帖子中的封面图
                    AssetImage(
                        assetPath = "cover/7.png",
                        contentDescription = "Album Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // ===== Fans also like =====
            Column(
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Text(
                    text = "Fans also like",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val fansAlsoLike = listOf(
                        Pair("YOASOBI", "avatar/1.png"),
                        Pair("Ado", "avatar/2.png"),
                        Pair("Fujii Kaze", "avatar/3.png"),
                        Pair("Aimer", "avatar/4.png"),
                        Pair("RADWIMPS", "avatar/5.png")
                    )

                    fansAlsoLike.forEach { (name, avatar) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(100.dp)
                        ) {
                            AssetImage(
                                assetPath = avatar,
                                contentDescription = name,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = name,
                                color = Color.White,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

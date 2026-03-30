package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Album
import com.example.myspotify.model.Artist
import com.example.myspotify.model.Playlist
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage

/**
 * YourLibraryTab View - 音乐库页面视图
 * 管理子页面导航
 */
@Composable
fun YourLibraryTabView(
    artists: List<Artist>,
    songs: List<Song>,
    albums: List<Album>,
    likedSongs: List<Song>,
    followedArtists: List<Artist>,
    onSongClick: (Song) -> Unit = {},
    onSongMenuClick: (Song) -> Unit = {},
    likedSongIds: List<String> = emptyList(),
    onToggleLike: (Song) -> Unit = {},
    userPlaylists: MutableList<Playlist> = mutableListOf()
) {
    // 子页面导航状态
    var showCreateTab by remember { mutableStateOf(false) }
    var showSearchTab by remember { mutableStateOf(false) }
    var showLikedSongsTab by remember { mutableStateOf(false) }
    var showArtistTab by remember { mutableStateOf(false) }
    var selectedArtist by remember { mutableStateOf<Artist?>(null) }
    var showAddArtistsTab by remember { mutableStateOf(false) }
    var showAddEventsTab by remember { mutableStateOf(false) }
    var showAddPodcastsTab by remember { mutableStateOf(false) }
    var showImportMusicTab by remember { mutableStateOf(false) }

    // 歌单创建相关状态
    var showPlaylistNameTab by remember { mutableStateOf(false) }
    var showUserPlaylistDetail by remember { mutableStateOf(false) }
    var selectedUserPlaylist by remember { mutableStateOf<Playlist?>(null) }
    var showAddToPlaylistTab by remember { mutableStateOf(false) }
    var playlistCounter by remember { mutableIntStateOf(1) }

    // 子页面：添加歌曲到歌单
    if (showAddToPlaylistTab && selectedUserPlaylist != null) {
        val playlist = selectedUserPlaylist!!
        AddToPlaylistTabView(
            playlistName = playlist.name,
            allSongs = songs,
            addedSongIds = playlist.songIds,
            onBack = { showAddToPlaylistTab = false },
            onAddSong = { song ->
                val index = userPlaylists.indexOfFirst { it.id == playlist.id }
                if (index >= 0 && !playlist.songIds.contains(song.id)) {
                    val updated = playlist.copy(songIds = playlist.songIds + song.id)
                    userPlaylists[index] = updated
                    selectedUserPlaylist = updated
                }
            }
        )
        return
    }

    // 子页面：用户创建的歌单详情
    if (showUserPlaylistDetail && selectedUserPlaylist != null) {
        val playlist = selectedUserPlaylist!!
        val playlistSongs = remember(playlist.songIds) {
            playlist.songIds.mapNotNull { id -> songs.find { it.id == id } }
        }
        PlaylistDetailTabView(
            playlist = playlist,
            playlistSongs = playlistSongs,
            onBack = { showUserPlaylistDetail = false; selectedUserPlaylist = null },
            onSongClick = onSongClick,
            onSongMenuClick = onSongMenuClick,
            onAddToPlaylist = { showAddToPlaylistTab = true },
            likedSongIds = likedSongIds,
            onToggleLike = onToggleLike
        )
        return
    }

    // 子页面：歌单命名
    if (showPlaylistNameTab) {
        PlaylistNameTabView(
            onCancel = { showPlaylistNameTab = false },
            onCreate = { name ->
                val newPlaylist = Playlist(
                    id = "user_playlist_${playlistCounter}",
                    name = name,
                    coverUrl = "",
                    creator = "You",
                    songIds = emptyList()
                )
                playlistCounter++
                userPlaylists.add(0, newPlaylist)
                showPlaylistNameTab = false
                selectedUserPlaylist = newPlaylist
                showUserPlaylistDetail = true
            }
        )
        return
    }

    // 子页面：艺术家详情
    if (showArtistTab && selectedArtist != null) {
        val artist = selectedArtist!!
        ArtistTabView(
            artist = artist,
            artistSongs = songs.filter { it.artistId == artist.id },
            artistAlbums = albums.filter { it.artistId == artist.id },
            onBack = { showArtistTab = false; selectedArtist = null },
            onSongClick = onSongClick,
            onSongMenuClick = onSongMenuClick,
            likedSongIds = likedSongIds,
            onToggleLike = onToggleLike
        )
        return
    }

    // 子页面：添加艺术家
    if (showAddArtistsTab) {
        AddArtistsTabView(
            allArtists = artists,
            followedArtists = followedArtists,
            onBack = { showAddArtistsTab = false }
        )
        return
    }

    // 子页面：添加活动和场馆
    if (showAddEventsTab) {
        AddEventsAndVenuesTabView(onBack = { showAddEventsTab = false })
        return
    }

    // 子页面：添加播客
    if (showAddPodcastsTab) {
        AddPodcastsTabView(onBack = { showAddPodcastsTab = false })
        return
    }

    // 子页面：导入音乐
    if (showImportMusicTab) {
        ImportYourMusicTabView(onBack = { showImportMusicTab = false })
        return
    }

    // 子页面：搜索音乐库
    if (showSearchTab) {
        SearchYourLibraryTabView(
            followedArtists = followedArtists,
            onCancel = { showSearchTab = false }
        )
        return
    }

    // 子页面：喜欢的歌曲
    if (showLikedSongsTab) {
        LikedSongsTabView(
            likedSongs = likedSongs,
            onBack = { showLikedSongsTab = false },
            onSongClick = onSongClick,
            onSongMenuClick = onSongMenuClick,
            likedSongIds = likedSongIds,
            onToggleLike = onToggleLike
        )
        return
    }

    var selectedTab by remember { mutableStateOf("Playlists") }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    IconButton(onClick = { showSearchTab = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showCreateTab = true }) {
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

                // Import music 提示（可点击）
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showImportMusicTab = true }
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

                // Liked Songs（可点击进入详情）
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLikedSongsTab = true }
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
                                    text = "Playlist • ${likedSongs.size} song${if (likedSongs.size != 1) "s" else ""}",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // 用户创建的歌单
                items(userPlaylists) { playlist ->
                    UserPlaylistItem(
                        playlist = playlist,
                        songCount = playlist.songIds.size,
                        onClick = {
                            selectedUserPlaylist = playlist
                            showUserPlaylistDetail = true
                        }
                    )
                }

                // 艺术家列表（可点击进入详情）
                items(artists) { artist ->
                    ArtistItem(
                        artist = artist,
                        onClick = {
                            selectedArtist = artist
                            showArtistTab = true
                        }
                    )
                }

                // Add artists 按钮（可点击）
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddArtistsTab = true }
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

                // Add events and venues 按钮（方形容器）
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddEventsTab = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(4.dp))
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
                            text = "Add events and venues",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Add podcasts 按钮（方形容器）
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddPodcastsTab = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(4.dp))
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
                            text = "Add podcasts",
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

        // CreateTab 弹窗（覆盖在内容之上）
        if (showCreateTab) {
            CreateTabView(
                onDismiss = { showCreateTab = false },
                onPlaylistCreate = { showPlaylistNameTab = true }
            )
        }
    }
}

/**
 * 用户创建的歌单项
 */
@Composable
fun UserPlaylistItem(playlist: Playlist, songCount: Int, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌单封面（音乐图标）
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF282828)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = playlist.name,
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = playlist.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Playlist · $songCount song${if (songCount != 1) "s" else ""}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * 艺术家项组件
 */
@Composable
fun ArtistItem(artist: Artist, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        AssetImage(
            assetPath = AssetMapper.artistAvatar(artist.id),
            contentDescription = artist.name,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
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

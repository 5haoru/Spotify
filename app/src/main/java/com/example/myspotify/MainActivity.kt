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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.data.AutoTestExporter
import com.example.myspotify.data.DataManager
import com.example.myspotify.model.Album
import com.example.myspotify.model.Artist
import com.example.myspotify.model.Playlist
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage
import com.example.myspotify.ui.common.MusicMenuView
import com.example.myspotify.ui.common.PlaylistMenuView
import com.example.myspotify.ui.home.PlayingTabView
import com.example.myspotify.ui.home.HomeTabView
import com.example.myspotify.ui.library.PlaylistDetailTabView
import com.example.myspotify.ui.library.YourLibraryTabView
import com.example.myspotify.ui.premium.PremiumTabView
import com.example.myspotify.ui.search.SearchTabView
import com.example.myspotify.ui.theme.MySpotifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MySpotifyTheme {
                MainScreen(
                    dataManager = DataManager(this),
                    autoTestExporter = AutoTestExporter(this)
                )
            }
        }
    }
}

@Composable
fun MainScreen(dataManager: DataManager, autoTestExporter: AutoTestExporter) {
    var selectedTab by remember { mutableStateOf(0) }
    var showPlayingTab by remember { mutableStateOf(false) }
    var showPlaylistDetail by remember { mutableStateOf(false) }
    var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }
    var showAlbumDetail by remember { mutableStateOf(false) }
    var selectedAlbum by remember { mutableStateOf<Album?>(null) }
    var showPlaylistMenu by remember { mutableStateOf(false) }
    var menuPlaylist by remember { mutableStateOf<Playlist?>(null) }
    var showMusicMenu by remember { mutableStateOf(false) }
    var menuSong by remember { mutableStateOf<Song?>(null) }
    val userPlaylists = remember { mutableStateListOf<Playlist>() }
    val savedPodcastIds = remember { mutableStateListOf<String>() }
    val savedAudiobookIds = remember { mutableStateListOf<String>() }

    val songs = remember { dataManager.getSongs() }
    val defaultSong = remember { dataManager.getSongById("song_013") }
    var selectedSong by remember { mutableStateOf(defaultSong) }
    val artists = remember { dataManager.getArtists() }
    val albums = remember { dataManager.getAlbums() }
    val playlists = remember { dataManager.getPlaylists() }
    val recentlyPlayedSongs = remember {
        dataManager.getUserData().recentlyPlayed.mapNotNull { dataManager.getSongById(it) }
    }
    val followedArtistIds = remember {
        mutableStateListOf<String>().apply { addAll(dataManager.getUserData().followedArtists) }
    }
    val followedArtists by remember {
        derivedStateOf { followedArtistIds.mapNotNull { dataManager.getArtistById(it) } }
    }

    // 关注/取消关注艺术家
    val onToggleFollow: (Artist) -> Unit = { artist ->
        if (followedArtistIds.contains(artist.id)) {
            followedArtistIds.remove(artist.id)
        } else {
            followedArtistIds.add(artist.id)
        }
    }
    val likedSongIds = remember {
        mutableStateListOf<String>().apply { addAll(dataManager.getUserData().likedSongs) }
    }
    val likedSongs by remember {
        derivedStateOf { likedSongIds.mapNotNull { dataManager.getSongById(it) } }
    }

    // AutoTest: 导出状态的辅助函数
    val exportState = {
        autoTestExporter.exportUserState(
            likedSongIds = likedSongIds.toList(),
            followedArtistIds = followedArtistIds.toList(),
            savedPodcastIds = savedPodcastIds.toList(),
            savedAudiobookIds = savedAudiobookIds.toList(),
            savedPlaylistIds = userPlaylists.map { it.id }
        )
        autoTestExporter.exportUserPlaylists(userPlaylists.toList())
        autoTestExporter.exportPlaybackState(
            currentSongId = selectedSong?.id,
            isPlaying = false,
            playMode = "SEQUENTIAL",
            shuffleEnabled = false
        )
    }

    // AutoTest: 状态变化时自动导出
    // 使用 hashCode 作为 key 来检测 userPlaylists 内容变化（包括 songIds 变更）
    val playlistsHash = userPlaylists.sumOf { it.songIds.size * 31 + it.hashCode() }
    LaunchedEffect(likedSongIds.size, followedArtistIds.size, playlistsHash,
        savedPodcastIds.size, savedAudiobookIds.size, selectedSong?.id) {
        exportState()
    }

    // 收藏/取消收藏歌曲
    val onToggleLike: (Song) -> Unit = { song ->
        if (likedSongIds.contains(song.id)) {
            likedSongIds.remove(song.id)
        } else {
            likedSongIds.add(song.id)
        }
    }

    // 通用的歌曲点击处理
    val onSongClick: (Song) -> Unit = { song ->
        selectedSong = song
        showPlayingTab = true
    }

    // 歌单点击处理
    val onPlaylistClick: (Playlist) -> Unit = { playlist ->
        selectedPlaylist = playlist
        showPlaylistDetail = true
    }

    // 专辑点击处理（复用歌单详情页的布局）
    val onAlbumClick: (Album) -> Unit = { album ->
        selectedAlbum = album
        showAlbumDetail = true
    }

    // 歌单菜单点击处理
    val onPlaylistMenuClick: (Playlist) -> Unit = { playlist ->
        menuPlaylist = playlist
        showPlaylistMenu = true
    }

    // 歌曲菜单点击处理
    val onSongMenuClick: (Song) -> Unit = { song ->
        menuSong = song
        showMusicMenu = true
    }

    // 显示歌单菜单
    if (showPlaylistMenu && menuPlaylist != null) {
        PlaylistMenuView(
            playlist = menuPlaylist!!,
            onBack = { showPlaylistMenu = false; menuPlaylist = null }
        )
        return
    }

    // 显示歌曲菜单
    if (showMusicMenu && menuSong != null) {
        MusicMenuView(
            song = menuSong!!,
            onBack = { showMusicMenu = false; menuSong = null }
        )
        return
    }

    // 显示全屏播放页面
    if (showPlayingTab) {
        PlayingTabView(
            song = selectedSong,
            onBack = { showPlayingTab = false },
            isLiked = selectedSong?.let { likedSongIds.contains(it.id) } ?: false,
            onLikeToggle = { selectedSong?.let { onToggleLike(it) } },
            userPlaylists = userPlaylists,
            likedSongsCount = likedSongIds.size,
            onAddSongToPlaylist = { s, playlist ->
                val index = userPlaylists.indexOfFirst { it.id == playlist.id }
                if (index >= 0 && !playlist.songIds.contains(s.id)) {
                    userPlaylists[index] = playlist.copy(songIds = playlist.songIds + s.id)
                }
            },
            onAddSongToLikedSongs = { s ->
                if (!likedSongIds.contains(s.id)) {
                    likedSongIds.add(s.id)
                }
            },
            onPrevious = {
                val currentIndex = songs.indexOfFirst { it.id == selectedSong?.id }
                if (currentIndex > 0) {
                    selectedSong = songs[currentIndex - 1]
                } else if (songs.isNotEmpty()) {
                    selectedSong = songs.last()
                }
            },
            onNext = {
                val currentIndex = songs.indexOfFirst { it.id == selectedSong?.id }
                if (currentIndex >= 0 && currentIndex < songs.size - 1) {
                    selectedSong = songs[currentIndex + 1]
                } else if (songs.isNotEmpty()) {
                    selectedSong = songs.first()
                }
            }
        )
        return
    }

    // 显示歌单详情页
    if (showPlaylistDetail && selectedPlaylist != null) {
        val playlist = selectedPlaylist!!
        val playlistSongs = remember(playlist.id) {
            playlist.songIds.mapNotNull { dataManager.getSongById(it) }
        }
        PlaylistDetailTabView(
            playlist = playlist,
            playlistSongs = playlistSongs,
            onBack = { showPlaylistDetail = false; selectedPlaylist = null },
            onSongClick = onSongClick,
            onPlaylistMenuClick = { onPlaylistMenuClick(playlist) },
            onSongMenuClick = onSongMenuClick,
            likedSongIds = likedSongIds,
            onToggleLike = onToggleLike
        )
        return
    }

    // 显示专辑详情页（复用歌单详情页）
    if (showAlbumDetail && selectedAlbum != null) {
        val album = selectedAlbum!!
        val albumPlaylist = Playlist(
            id = album.id,
            name = album.name,
            coverUrl = album.coverUrl,
            creator = album.artistName,
            description = "${album.releaseYear}",
            songIds = album.songIds
        )
        val albumSongs = remember(album.id) {
            album.songIds.mapNotNull { dataManager.getSongById(it) }
        }
        PlaylistDetailTabView(
            playlist = albumPlaylist,
            playlistSongs = albumSongs,
            onBack = { showAlbumDetail = false; selectedAlbum = null },
            onSongClick = onSongClick,
            onPlaylistMenuClick = { onPlaylistMenuClick(albumPlaylist) },
            onSongMenuClick = onSongMenuClick,
            likedSongIds = likedSongIds,
            onToggleLike = onToggleLike
        )
        return
    }

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(
                    song = selectedSong,
                    isLiked = selectedSong?.let { likedSongIds.contains(it.id) } ?: false,
                    onLikeToggle = { selectedSong?.let { onToggleLike(it) } },
                    onClick = { showPlayingTab = true }
                )
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
                0 -> HomeTabView(
                    songs = songs,
                    albums = albums,
                    playlists = playlists,
                    artists = artists,
                    recentlyPlayedSongs = recentlyPlayedSongs,
                    onSongClick = onSongClick,
                    onPlaylistClick = onPlaylistClick,
                    onAlbumClick = onAlbumClick,
                    onSongMenuClick = onSongMenuClick,
                    likedSongIds = likedSongIds,
                    onToggleLike = onToggleLike,
                    savedPodcastIds = savedPodcastIds,
                    savedAudiobookIds = savedAudiobookIds
                )
                1 -> SearchTabView(
                    songs = songs,
                    artists = artists,
                    albums = albums,
                    playlists = playlists,
                    recentlyPlayedSongs = recentlyPlayedSongs,
                    followedArtists = followedArtists,
                    onSongClick = onSongClick
                )
                2 -> YourLibraryTabView(
                    artists = artists,
                    songs = songs,
                    albums = albums,
                    likedSongs = likedSongs,
                    followedArtists = followedArtists,
                    onSongClick = onSongClick,
                    onSongMenuClick = onSongMenuClick,
                    likedSongIds = likedSongIds,
                    onToggleLike = onToggleLike,
                    userPlaylists = userPlaylists,
                    followedArtistIds = followedArtistIds,
                    onToggleFollow = onToggleFollow,
                    savedPodcastIds = savedPodcastIds,
                    savedAudiobookIds = savedAudiobookIds
                )
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
fun MiniPlayer(
    song: Song? = null,
    isLiked: Boolean = false,
    onLikeToggle: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val coverPath = if (song != null) AssetMapper.songCover(song) else "cover/13.png"
    val title = song?.title ?: "IRIS OUT"
    val artist = song?.artistName ?: "Kenshi Yonezu"

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = Color(0xFF8B0000),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssetImage(
                assetPath = coverPath,
                contentDescription = "Now Playing Cover",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = artist,
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
                IconButton(onClick = onLikeToggle) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = if (isLiked) Color(0xFF1DB954) else Color.White
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

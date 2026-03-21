# MySpotify

一个基于 Android Jetpack Compose 的 Spotify 克隆应用。

## 项目结构

```
app/src/main/java/com/example/myspotify/
├── model/              # 数据模型
│   ├── Song.kt
│   ├── Artist.kt
│   ├── Album.kt
│   ├── Playlist.kt
│   ├── UserData.kt
│   └── PlaybackState.kt
├── data/               # 数据管理
│   └── DataManager.kt
├── ui/                 # UI 页面
│   ├── home/
│   │   └── HomeTabView.kt
│   ├── search/
│   │   └── SearchTabView.kt
│   ├── library/
│   │   └── YourLibraryTabView.kt
│   └── premium/
│       └── PremiumTabView.kt
└── MainActivity.kt     # 主入口

app/src/main/assets/data/  # 初始数据
├── songs.json
├── artists.json
├── albums.json
├── playlists.json
├── user_data.json
└── playback_state.json
```

## 功能特性

- **主页 (HomeTab)**: 展示歌曲列表、专辑推荐、分类筛选
- **搜索 (SearchTab)**: 搜索栏、分类卡片、发现新内容
- **音乐库 (YourLibraryTab)**: 播放列表、艺术家、喜欢的歌曲
- **Premium (PremiumTab)**: Premium 订阅页面

## 技术栈

- Kotlin 1.9.25
- Jetpack Compose
- Material Design 3
- Gson (JSON 解析)
- MVP 架构模式

## 构建说明

```bash
./gradlew build
```

构建成功后可在 Android 设备或模拟器上运行。

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Vue3 音频播放器 - 音量控制</title>
  <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    body {
      background: linear-gradient(135deg, #1a2a6c, #b21f1f, #1a2a6c);
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 20px;
    }

    .container {
      max-width: 800px;
      width: 100%;
      text-align: center;
    }

    h1 {
      color: white;
      font-size: 2.5rem;
      margin-bottom: 10px;
      text-shadow: 0 2px 10px rgba(0,0,0,0.3);
    }

    .subtitle {
      color: rgba(255,255,255,0.85);
      margin-bottom: 30px;
      font-weight: 300;
      font-size: 1.1rem;
    }

    .player-container {
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(10px);
      border-radius: 20px;
      padding: 30px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
      border: 1px solid rgba(255, 255, 255, 0.1);
      margin-bottom: 30px;
    }

    .album-art {
      width: 200px;
      height: 200px;
      border-radius: 50%;
      margin: 0 auto 25px;
      overflow: hidden;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
      border: 3px solid rgba(255, 255, 255, 0.1);
      background: #222;
      position: relative;
    }

    .album-art img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .song-info {
      margin-bottom: 25px;
    }

    .song-title {
      color: white;
      font-size: 1.8rem;
      margin-bottom: 5px;
    }

    .song-artist {
      color: rgba(255, 255, 255, 0.7);
      font-size: 1.2rem;
    }

    .progress-container {
      width: 100%;
      height: 6px;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 10px;
      margin: 25px 0;
      cursor: pointer;
      position: relative;
    }

    .progress-bar {
      height: 100%;
      width: 0%;
      background: linear-gradient(to right, #ff7e5f, #feb47b);
      border-radius: 10px;
      transition: width 0.1s linear;
    }

    .progress-time {
      display: flex;
      justify-content: space-between;
      color: rgba(255, 255, 255, 0.7);
      font-size: 0.9rem;
      margin-top: 5px;
    }

    .controls {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 25px;
      margin: 25px 0;
    }

    .control-btn {
      background: rgba(255, 255, 255, 0.1);
      border: none;
      width: 60px;
      height: 60px;
      border-radius: 50%;
      color: white;
      font-size: 1.5rem;
      cursor: pointer;
      display: flex;
      justify-content: center;
      align-items: center;
      transition: all 0.3s ease;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    }

    .control-btn:hover {
      background: rgba(255, 255, 255, 0.2);
      transform: scale(1.05);
    }

    .control-btn.play-pause {
      width: 70px;
      height: 70px;
      font-size: 1.8rem;
      background: linear-gradient(135deg, #ff7e5f, #feb47b);
    }

    .volume-control {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 15px;
      margin-top: 20px;
    }

    .volume-icon {
      color: white;
      font-size: 1.5rem;
      width: 40px;
      cursor: pointer;
    }

    .volume-slider-container {
      flex: 1;
      max-width: 300px;
      display: flex;
      align-items: center;
    }

    .volume-slider {
      -webkit-appearance: none;
      width: 100%;
      height: 6px;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 10px;
      outline: none;
    }

    .volume-slider::-webkit-slider-thumb {
      -webkit-appearance: none;
      width: 18px;
      height: 18px;
      border-radius: 50%;
      background: white;
      cursor: pointer;
      box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
      transition: all 0.2s;
    }

    .volume-slider::-webkit-slider-thumb:hover {
      transform: scale(1.2);
    }

    .volume-value {
      color: white;
      min-width: 40px;
      text-align: center;
      font-weight: bold;
      background: rgba(0, 0, 0, 0.3);
      padding: 3px 8px;
      border-radius: 20px;
      font-size: 0.9rem;
    }

    .volume-percentage {
      font-size: 0.8em;
    }

    .visualizer {
      display: flex;
      justify-content: center;
      align-items: flex-end;
      height: 80px;
      gap: 3px;
      margin-top: 20px;
    }

    .visualizer-bar {
      width: 8px;
      background: linear-gradient(to top, #ff7e5f, #feb47b);
      border-radius: 4px 4px 0 0;
      transition: height 0.2s ease;
    }

    .song-list {
      background: rgba(255, 255, 255, 0.05);
      border-radius: 15px;
      padding: 20px;
      margin-top: 20px;
    }

    .song-item {
      display: flex;
      align-items: center;
      padding: 12px 15px;
      border-radius: 10px;
      margin-bottom: 10px;
      cursor: pointer;
      transition: all 0.3s ease;
      color: rgba(255, 255, 255, 0.8);
    }

    .song-item:hover {
      background: rgba(255, 255, 255, 0.1);
    }

    .song-item.active {
      background: linear-gradient(to right, rgba(255, 126, 95, 0.3), rgba(254, 180, 123, 0.3));
      color: white;
    }

    .song-item .play-icon {
      margin-right: 15px;
      font-size: 0.9rem;
      color: #ff7e5f;
    }

    .song-item .song-details {
      flex: 1;
      text-align: left;
    }

    .song-item .song-name {
      font-weight: 500;
    }

    .song-item .artist {
      font-size: 0.85rem;
      color: rgba(255, 255, 255, 0.6);
    }

    .song-item .duration {
      font-size: 0.9rem;
      color: rgba(255, 255, 255, 0.7);
    }

    .instructions {
      background: rgba(0, 0, 0, 0.2);
      border-radius: 10px;
      padding: 15px;
      margin-top: 30px;
      color: rgba(255, 255, 255, 0.8);
      font-size: 0.9rem;
      text-align: left;
    }

    .instructions h3 {
      color: white;
      margin-bottom: 10px;
      font-size: 1.1rem;
    }

    .instructions ul {
      padding-left: 20px;
    }

    .instructions li {
      margin-bottom: 8px;
      line-height: 1.5;
    }

    @media (max-width: 600px) {
      .player-container {
        padding: 20px;
      }

      .album-art {
        width: 150px;
        height: 150px;
      }

      .controls {
        gap: 15px;
      }

      .control-btn {
        width: 50px;
        height: 50px;
        font-size: 1.2rem;
      }

      .control-btn.play-pause {
        width: 60px;
        height: 60px;
        font-size: 1.5rem;
      }

      .song-title {
        font-size: 1.5rem;
      }
    }
  </style>
</head>
<body>
<div id="app">
  <div class="container">
    <h1>Vue3 音频播放器</h1>
    <div class="subtitle">使用Vue3 Composition API实现音量控制</div>

    <div class="player-container">
      <div class="album-art">
        <img :src="currentSong.cover" alt="Album Art">
      </div>

      <div class="song-info">
        <h2 class="song-title">{{ currentSong.title }}</h2>
        <div class="song-artist">{{ currentSong.artist }}</div>
      </div>

      <div class="progress-container" @click="setProgress">
        <div class="progress-bar" :style="{width: progressPercent + '%'}"></div>
      </div>
      <div class="progress-time">
        <span>{{ formatTime(currentTime) }}</span>
        <span>{{ formatTime(duration) }}</span>
      </div>

      <div class="controls">
        <button class="control-btn" @click="prevSong">
          <i class="fas fa-step-backward"></i>
        </button>
        <button class="control-btn play-pause" @click="togglePlay">
          <i :class="isPlaying ? 'fas fa-pause' : 'fas fa-play'"></i>
        </button>
        <button class="control-btn" @click="nextSong">
          <i class="fas fa-step-forward"></i>
        </button>
      </div>

      <div class="volume-control">
        <div class="volume-icon" @click="toggleMute">
          <i v-if="isMuted" class="fas fa-volume-mute"></i>
          <i v-else-if="volume < 0.5" class="fas fa-volume-down"></i>
          <i v-else class="fas fa-volume-up"></i>
        </div>
        <div class="volume-slider-container">
          <input
              type="range"
              class="volume-slider"
              min="0"
              max="1"
              step="0.01"
              v-model="volume"
              @input="updateVolume"
          >
        </div>
        <div class="volume-value">
          {{ Math.round(volume * 100) }}<span class="volume-percentage">%</span>
        </div>
      </div>

      <div class="visualizer">
        <div
            v-for="(bar, index) in visualizerBars"
            :key="index"
            class="visualizer-bar"
            :style="{ height: bar + 'px' }"
        ></div>
      </div>
    </div>

    <div class="song-list">
      <div
          v-for="(song, index) in songs"
          :key="index"
          class="song-item"
          :class="{ active: currentSongIndex === index }"
          @click="selectSong(index)"
      >
        <div class="play-icon">
          <i v-if="currentSongIndex === index && isPlaying" class="fas fa-volume-up"></i>
          <i v-else class="fas fa-music"></i>
        </div>
        <div class="song-details">
          <div class="song-name">{{ song.title }}</div>
          <div class="artist">{{ song.artist }}</div>
        </div>
        <div class="duration">{{ song.duration }}</div>
      </div>
    </div>

    <div class="instructions">
      <h3>音量控制实现说明：</h3>
      <ul>
        <li>使用Vue3的ref和computed来管理音频状态</li>
        <li>通过audio元素的volume属性控制音量（0.0到1.0）</li>
        <li>实现音量滑块：v-model绑定到volume变量</li>
        <li>添加静音功能：通过设置volume为0或恢复之前音量</li>
        <li>音量图标根据当前音量状态动态变化</li>
        <li>添加了音量数值百分比显示</li>
      </ul>
    </div>
  </div>
</div>

<script>
  const { createApp, ref, computed, onMounted } = Vue;

  createApp({
    setup() {
      // 音频元素引用
      const audioPlayer = ref(null);

      // 播放状态
      const isPlaying = ref(false);

      // 音量状态
      const volume = ref(0.7);
      const isMuted = ref(false);
      const prevVolume = ref(0.7);

      // 歌曲进度
      const currentTime = ref(0);
      const duration = ref(0);

      // 歌曲列表
      const songs = ref([
        {
          title: "Summer Vibes",
          artist: "Ocean Waves",
          duration: "3:45",
          cover: "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80",
          file: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        },
        {
          title: "Mountain Echo",
          artist: "Nature Sounds",
          duration: "4:20",
          cover: "https://images.unsplash.com/photo-1426604966848-d7adac402bff?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80",
          file: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
        },
        {
          title: "City Lights",
          artist: "Urban Beats",
          duration: "3:15",
          cover: "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80",
          file: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
        },
        {
          title: "Desert Wind",
          artist: "Sandy Dunes",
          duration: "5:10",
          cover: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80",
          file: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
        }
      ]);

      // 当前播放歌曲索引
      const currentSongIndex = ref(0);

      // 可视化效果条
      const visualizerBars = ref(Array(20).fill(20));

      // 计算当前歌曲
      const currentSong = computed(() => {
        return songs.value[currentSongIndex.value];
      });

      // 计算进度百分比
      const progressPercent = computed(() => {
        return duration.value ? (currentTime.value / duration.value) * 100 : 0;
      });

      // 初始化音频
      onMounted(() => {
        audioPlayer.value = new Audio();
        audioPlayer.value.src = currentSong.value.file;
        audioPlayer.value.volume = volume.value;

        audioPlayer.value.addEventListener('timeupdate', () => {
          currentTime.value = audioPlayer.value.currentTime;
        });

        audioPlayer.value.addEventListener('loadedmetadata', () => {
          duration.value = audioPlayer.value.duration;
        });

        audioPlayer.value.addEventListener('ended', nextSong);

        // 可视化效果动画
        setInterval(() => {
          if (isPlaying.value) {
            visualizerBars.value = visualizerBars.value.map(() =>
                Math.floor(Math.random() * 60) + 20
            );
          }
        }, 200);
      });

      // 播放/暂停切换
      const togglePlay = () => {
        if (isPlaying.value) {
          audioPlayer.value.pause();
        } else {
          audioPlayer.value.play();
        }
        isPlaying.value = !isPlaying.value;
      };

      // 下一首
      const nextSong = () => {
        currentSongIndex.value = (currentSongIndex.value + 1) % songs.value.length;
        loadSong();
      };

      // 上一首
      const prevSong = () => {
        currentSongIndex.value = (currentSongIndex.value - 1 + songs.value.length) % songs.value.length;
        loadSong();
      };

      // 选择歌曲
      const selectSong = (index) => {
        currentSongIndex.value = index;
        loadSong();
        isPlaying.value = true;
      };

      // 加载歌曲
      const loadSong = () => {
        if (audioPlayer.value) {
          audioPlayer.value.pause();
          audioPlayer.value.src = currentSong.value.file;
          audioPlayer.value.volume = volume.value;
          audioPlayer.value.play().then(() => {
            isPlaying.value = true;
          }).catch(e => {
            console.error("播放失败:", e);
          });
        }
      };

      // 设置进度
      const setProgress = (e) => {
        if (!audioPlayer.value) return;

        const width = e.currentTarget.clientWidth;
        const clickX = e.offsetX;
        const duration = audioPlayer.value.duration;

        audioPlayer.value.currentTime = (clickX / width) * duration;
      };

      // 更新音量
      const updateVolume = () => {
        if (audioPlayer.value) {
          audioPlayer.value.volume = volume.value;
          isMuted.value = volume.value === 0;
        }
      };

      // 静音/取消静音
      const toggleMute = () => {
        if (isMuted.value) {
          // 取消静音
          volume.value = prevVolume.value > 0 ? prevVolume.value : 0.5;
          isMuted.value = false;
        } else {
          // 静音
          prevVolume.value = volume.value;
          volume.value = 0;
          isMuted.value = true;
        }
        updateVolume();
      };

      // 格式化时间
      const formatTime = (seconds) => {
        if (isNaN(seconds)) return "0:00";

        const minutes = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
      };

      return {
        isPlaying,
        volume,
        isMuted,
        currentTime,
        duration,
        songs,
        currentSongIndex,
        currentSong,
        progressPercent,
        visualizerBars,
        togglePlay,
        nextSong,
        prevSong,
        selectSong,
        setProgress,
        updateVolume,
        toggleMute,
        formatTime
      };
    }
  }).mount('#app');
</script>
</body>
</html>
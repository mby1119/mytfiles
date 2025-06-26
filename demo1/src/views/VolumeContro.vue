<template>
  <div class="volume-control">
    <el-button class="control-btn play-btn" icon="VideoPlay" v-if="data.playing===true" @click="open_play"></el-button>
    <el-button class="control-btn play-btn" icon="VideoPause" v-if="data.playing===false" @click="close_play"></el-button>
    <!-- 静音按钮 -->
    <el-button icon="Fold" style="color: #747bff" @click="toggleMute">
      <i :class="volumeIcon"></i>
    </el-button>

    <!-- 音量滑块 -->
    <input
        type="range"
        min="0"
        max="1"
        step="0.01"
        v-model="volume"
        @input="updateVolume"
    >

    <!-- 音量百分比 -->
    <span>{{ volumePercentage }}%</span>
  </div>
</template>

<script setup>
import { reactive,ref, computed, onMounted } from 'vue'
import {useRoute} from "vue-router";

const data=reactive({
  music_id:"null",
  playing:true,
})

const product = ref(null);
const route = useRoute();

// 音频元素
const audioRef = ref(null)

// 音量状态
const volume = ref(0.7) // 默认音量70%
const isMuted = ref(false)
const prevVolume = ref(0.7) // 用于保存静音前的音量

// 计算属性
const volumePercentage = computed(() => Math.round(volume.value * 100))
const volumeIcon = computed(() => {
  if (isMuted.value) return 'fas fa-volume-mute'
  if (volume.value < 0.5) return 'fas fa-volume-down'
  return 'fas fa-volume-up'
})

// 初始化音频
onMounted(() => {
  audioRef.value = new Audio('your-audio-file.mp3')
  audioRef.value.volume = volume.value

  product.value = {
    id: route.query.id,
  };
  data.music_id=product.value.id;
})

// 更新音量
const updateVolume = () => {
  if (!audioRef.value) return

  // 设置audio元素音量
  audioRef.value.volume = volume.value

  // 音量>0时自动取消静音
  if (volume.value > 0) {
    isMuted.value = false
  }
}

// 切换静音
const toggleMute = () => {
  if (isMuted.value) {
    // 取消静音：恢复之前音量
    volume.value = prevVolume.value > 0 ? prevVolume.value : 0.5
    isMuted.value = false
  } else {
    // 静音：保存当前音量并设为0
    prevVolume.value = volume.value
    volume.value = 0
    isMuted.value = true
  }

  updateVolume()
}

const open_play=()=>{
  data.playing=false;
}
const close_play=()=>{
  data.playing=true;
}
</script>

<style scoped>
.volume-control {
  display: flex;
  align-items: center;
  gap: 10px;
}

input[type="range"] {
  width: 100px;
  height: 6px;
  -webkit-appearance: none;
  background: #ddd;
  border-radius: 3px;
  outline: none;
}

input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #4285f4;
  cursor: pointer;
}

span {
  min-width: 40px;
  text-align: center;
  font-family: monospace;
  color: white;
}

.control-btn {
  width: 50px;
  height: 50px;
  border: none;
  border-radius: 50%;
  background: #fff;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}
.play-btn {
  width: 50px;
  height: 50px;
  background: #2196F3;
  color: white;
}
.control-btn:hover {
  transform: scale(1.1);
}

</style>
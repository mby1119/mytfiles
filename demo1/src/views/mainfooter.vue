<template>
    <!--音乐卡面-->
  <div class="music-player">
    <!-- 左侧封面部分 -->
    <div class="cover-section">
      <img :src="data.cover"  alt="专辑封面"  :class="isplays">
    </div>
    <!-- 中间音乐信息部分 -->
    <div class="info-section">
      <div class="song-info">
        <div>{{ data.name }}</div>
        <div>{{ data.singer }}</div>
      </div>
    </div>
    <!-- 右侧控制按钮部分 -->
    <div>
      <!-- 音乐播放器audio -->
      <audio ref="audioElement" :src="data.audiourl" type="audio/mpeg"></audio>
    </div>
    <div class="control-section">
      <el-button class="control-btn" icon="ArrowLeftBold" @click="playon"></el-button>
      <el-button class="control-btn play-btn" icon="VideoPlay" v-if="data.playing===true" @click="open_play"></el-button>
      <el-button class="control-btn play-btn" icon="VideoPause" v-if="data.playing===false" @click="close_play"></el-button>
      <el-button class="control-btn" icon="ArrowRightBold" @click="playup"></el-button>
      <el-button icon="VideoPause" ></el-button>
      <el-button icon="Fold" @click="playlist"></el-button>
    </div>
    <!-- 播放列表部分 -->
    <el-dialog v-model="data.formVIsible" title="播放列表" width="500" destroy-on-close>
      <el-table :data="data.tableData" style="width: 100%;">
        <el-table-column prop="name" label="歌名" width="180" />
        <el-table-column prop="singer" label="歌手" width="180" />
        <el-table-column label="操作" >
          <template #default="scope">
            <el-button type="primary" icon="VideoPlay" circle @click="get_info(scope.$index)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive,ref} from "vue";
import request from "../utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {useCounterStore} from "../utils/star.js";

const store = useCounterStore()

const  data=reactive({
  playing:true,
  formVIsible:false,
  sum:0,
  len:-1,
  name:null,
  singer:null,
  audiourl:null,
  cover:null,
  timeop:null,
  timeed:null,
  tableData:[]
})

const playload=()=>{
  request.get('/playlist/selectall').then(res=>{
    if (res.code === '200') {
      data.tableData=res.data
      data.name=res.data[data.sum].name
      data.singer=res.data[data.sum].singer
      data.audiourl=res.data[data.sum].audiourl
      data.cover=res.data[data.sum].cover
      /*判断是否有新歌播放(还是不对)re_play()*/
      data.len=res.data.length
    } else {
      ElMessage.error(res.msg)
    }
  })
}
playload();

const  audioElement =ref(null);
const isplay=ref(false);
const isplays=ref("album-cover2");


const open_play=()=>{
  audioElement.value.play();
  isplays.value="album-cover1";
  data.playing=false;
}
const close_play=()=>{
  audioElement.value.pause();
  isplays.value="album-cover2";
  data.playing=true;
}
const playup=async()=>{
  try {
    if(data.sum+1===data.len)
      data.sum=0;
    else
      data.sum++;
    isplays.value="album-cover1";
    playload();
    // 等待音频元数据加载完成
    await new Promise((resolve) => {
      audioElement.value.addEventListener('loadedmetadata', resolve);
    });
    audioElement.value.play();
    data.playing=false;
  }catch (error) {
    ElMessage.error("歌曲切换失败: " + error.message);
  }

}
const playon=async()=>{
  try {
    if(data.sum-1===-1)
      data.sum+=data.len-1;
    else
      data.sum--;
    isplays.value="album-cover1";
    playload();
    // 等待音频元数据加载完成
    await new Promise((resolve) => {
      audioElement.value.addEventListener('loadedmetadata', resolve);
    });
    audioElement.value.play();
    data.playing=false;
  }catch (error) {
    ElMessage.error("歌曲切换失败: " + error.message);
  }
}

const re_play=async()=>{
  try {
    data.sum=0;
    isplays.value="album-cover1";
    playload();
    // 等待音频元数据加载完成
    await new Promise((resolve) => {
      audioElement.value.addEventListener('loadedmetadata', resolve);
    });
    audioElement.value.play();
    data.playing=false;
  }catch (error) {
    ElMessage.error("歌曲播放失败: " + error.message);
  }
}

const get_info=async($index)=>{
  try {
    data.sum=$index;
    isplays.value="album-cover1";
    data.formVIsible=false;
    playload();
    console.log(store.message+' 123');
    // 等待音频元数据加载完成
    await new Promise((resolve) => {
      audioElement.value.addEventListener('loadedmetadata', resolve);
    });
    audioElement.value.play();
    data.playing=false;
  }catch (error) {
    ElMessage.error("歌曲播放失败: " + error.message);
  }
}

const playlist=()=>{
    data.formVIsible=true;
}

const del = (id) => {
  ElMessageBox.confirm('sure?','sure',{type:'warning'}).then(res =>{
    request.delete('/playlist/delete/'+id).then(res => {
      if (res.code === '200') {
        ElMessage.success('success')
        playload()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err =>{})
}
</script>

<style scoped>
.music-player {
  display: flex;
  align-items: center;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 0 0 10px 0;
  gap: 20px;
}

.cover-section {
  flex: 2;
  height: 100%;
}

.album-cover1 {
  width: 85px;
  height: 85px;
  margin-top: 15px;
  margin-left: 50px;
  object-fit: cover;
  border-radius: 50%;
  align-items: center;
  animation: zhuan 5s infinite linear;
  animation-play-state: running;
}

.album-cover2 {
  width: 85px;
  height: 85px;
  margin-top: 15px;
  margin-left: 50px;
  object-fit: cover;
  border-radius: 50%;
  align-items: center;
  animation: zhuan 5s infinite linear;
  animation-play-state: paused;
}

.info-section {
  flex: 5;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

}

.song-title {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.artist {
  margin: 5px 0 0;
  color: #666;
  font-size: 14px;
}

.progress-container {
  width: 100%;
  height: 4px;
  background: #ddd;
  border-radius: 2px;
  margin: 10px 0;
}

.progress-bar {
  height: 100%;
  background: #2196F3;
  border-radius: 2px;
  transition: width 0.3s ease;
}

.time-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #666;
}

.control-section {
  flex: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  padding-right: 40px;
}

.control-btn {
  width: 40px;
  height: 40px;
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

@keyframes zhuan{
  0%{
    transform: rotate(0deg);
  }
  100%{
    transform: rotate(360deg);
  }
}

</style>


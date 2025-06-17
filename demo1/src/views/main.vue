<template>
  <div class="background" :style="{ backgroundImage: `url(${currentBg})` }">
    <div class="controls">
      <button
          v-for="(bg, index) in backgrounds"
          :key="index"
          @click="currentBg = bg"
      >
        背景 {{ index + 1 }}
      </button>
    </div>
    <div class="main">
      <el-container>
        <!--header-->
        <el-header>
          <mainheader></mainheader>
        </el-header>

        <!--body-->
        <el-container>
          <!--left-->
          <el-aside style="width: 150px; background: rgba(255, 255, 255, 0.6); border-radius: 0  0 0 10px;" >
            <el-menu style="background: rgba(255, 255, 255, 0.6)" router :default-active="router.currentRoute.value.path">
              <el-menu-item index="/main/home">主页</el-menu-item>
              <el-menu-item index="/main/mymusic">我的音乐</el-menu-item>
              <el-menu-item index="/main/order">订单</el-menu-item>
              <el-menu-item index="/main/setting">聊天</el-menu-item>
              <el-menu-item index="/main/upload">file</el-menu-item>
            </el-menu>
          </el-aside>
          <!--right-->
          <el-container>
            <el-main style="background: rgba(255, 255, 255, 0.6)">
              <RouterView/>
            </el-main>
            <el-footer style="padding: 0;height: 120px;background: rgba(255, 255, 255, 0.6)">
              <mainfooter></mainfooter>
            </el-footer>
          </el-container>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script setup>
import router from "../router/index.js";
import mainheader from "./mainheader.vue";
import mainfooter from "./mainfooter.vue";

import { ref } from 'vue';

// 本地图片（使用import方式）
import bg1 from '../assets/img/12.png';
import bg2 from '../assets/img/8.jpg';
import bg3 from '../assets/img/12.png';
const backgrounds = ref([bg1, bg2, bg3]);
const currentBg = ref(bg1); // 默认背景

</script>

<style scoped>
.background{
  display: flex;
  width: 100vw;
  height: 100vh;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  transition: background-image 0.5s ease; /* 平滑过渡效果 */
}
.main{
  width: 78%;
  height: 75%;
  display: flex;
  margin: 12vh auto 0;
  border-radius: 10px;
  background: rgba(135, 206, 235, 0.6); /* 半透明背景 */
  backdrop-filter: blur(1px); /* 关键：毛玻璃效果 */
}
.controls {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
}
button {
  padding: 8px 15px;
  background: rgba(0,0,0,0.7);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
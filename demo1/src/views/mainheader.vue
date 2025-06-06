<template >
  <div style="display: flex; align-items: center; justify-content: center;">
    <div style="padding: 10px;font-size: 25px;flex: 7">
      我的音乐播放器
    </div>
    <button class="circle-dot" @click="setdeep"></button>
  </div>
</template>

<script setup>
const setdeep =()=>{

}
</script>

<style>
.circle-dot {
  width: 24px;
  height: 24px;
  border-radius: 50%;  /* 关键属性：圆形 */
  background-color: #3498db; /* 默认蓝色 */
  box-shadow: 0 2px 5px rgba(0,0,0,0.2);
}
</style>
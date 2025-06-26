<!-- ProductDetail.vue -->
<template>
  <div class="background" :style="{ backgroundImage: `url(${currentBg})` }">
    <div class="mbl">
      <div class="controls">
        <button class="bg_button"
            v-for="(bg, index) in backgrounds"
            :key="index"
            @click="currentBg = bg"
        >
          背景 {{ index + 1 }}
        </button>
      </div>
      <div class="min_body">
        <div class="left_item">
          <div class="item_word"></div>
          <div class="item_time"></div>
        </div>
        <div class="right_item">
          <div class="item_infos"></div>
          <div class="item_buttons">
              <VolumeContro></VolumeContro>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive,ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import bg1 from '../assets/img/12.png';
import bg2 from '../assets/img/8.jpg';
import bg3 from '../assets/img/12.png';
import VolumeContro from "./VolumeContro.vue";

const backgrounds = ref([bg1, bg2, bg3]);
const currentBg = ref(bg1);

const route = useRoute();
const product = ref(null);


const data=reactive({
  music_id:"null",
  playing:true,
})

onMounted(() => {
  product.value = {
    id: route.query.id,
  };
  data.music_id=product.value.id;
});

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

.mbl{
  display: flex;
  width: 100vw;
  height: 100vh;
  background-size: cover;
  background: rgba(35, 35, 35, 0.8) no-repeat center;/* 半透明背景 */
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

.bg_button {
  padding: 8px 15px;
  background: rgba(0,0,0,0.7);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.min_body{
  display: flex;
  margin: auto;
  width: 90%;
  height: 80vh;
}

.left_item{
  display: grid;
  flex: 65%;
  grid-template-rows: 80% 20%;
}
.right_item{
  display: grid;
  flex: 35%;
  grid-template-rows: 70% 30%;
}
.item_word{
  display: flex;
  margin: 0;
  background-color: white;
 }
.item_time{
  display: flex;
  margin: 0;
  background-color: red;
}
.item_infos{
  display: flex;
  margin: 0;
  background-color: blue;
}
.item_buttons{
  display: flex;
  margin: 0;
  padding: 40px;
  align-items: center;
}


</style>
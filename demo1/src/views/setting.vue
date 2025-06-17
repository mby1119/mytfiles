<template>
  <div
      ref="messageContainer"
      class="message-box"
  ></div>

  <!-- 绑定 ref 和回车事件 -->
  <input
      ref="messageInput"
      v-model="inputMessage"
      type="text"
      class="message-input"
      placeholder="请输入消息"
      @keyup.enter="sendMessage"
  >

  <button @click="sendMessage" class="send-button">发送</button>
</template>



<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
const messageContainer = ref(null);
const messageInput = ref(null);
const inputMessage = ref('');

// WebSocket 实例
const socket = ref(null);

// 初始化 WebSocket
onMounted(() => {
  socket.value = new WebSocket("ws://localhost:8081/ws");

  socket.value.onopen = () => {
    appendMessage("连接已建立");
  };

  socket.value.onmessage = (event) => {
    appendMessage(`收到: ${event.data}`);
  };

  socket.value.onclose = () => {
    appendMessage("连接已关闭");
  };
});

// 清理 WebSocket
onBeforeUnmount(() => {
  if (socket.value) {
    socket.value.close();
  }
});

// 添加消息到容器
function appendMessage(message) {
  if (!messageContainer.value) return;

  const messageElement = document.createElement('div');
  messageElement.textContent = message;
  messageContainer.value.appendChild(messageElement);

  // 自动滚动到底部
  messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
}

// 发送消息
function sendMessage() {
  if (!socket.value || !inputMessage.value.trim()) return;

  socket.value.send(inputMessage.value);
  appendMessage(`发送: ${inputMessage.value}`);
  inputMessage.value = ''; // 清空输入框
}
</script>

<style scoped>
.message-box {
  width: 500px;
  height: 300px;
  border: 1px solid #ccc;
  overflow-y: scroll;
  margin-bottom: 10px;
  padding: 10px;
}

.message-input {
  width: 400px;
  margin-right: 10px;
  padding: 5px;
}

.send-button {
  padding: 5px 15px;
  cursor: pointer;
}
</style>
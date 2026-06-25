<script setup>
import { ref, nextTick } from 'vue'
import api from '@/api/api'

const isOpen = ref(false)
const message = ref('')
const messages = ref([])
const error = ref('')
const loading = ref(false)
const chatBody = ref(null)

function toggleChat() {
  isOpen.value = !isOpen.value
}

async function scrollToBottom() {
  await nextTick()

  if (chatBody.value) {
    chatBody.value.scrollTop = chatBody.value.scrollHeight
  }

  setTimeout(() => {
    if (chatBody.value) {
      chatBody.value.scrollTop = chatBody.value.scrollHeight
    }
  }, 50)
}

async function sendMessage() {
  if (!message.value.trim()) {
    return
  }

  const userMessage = message.value

  messages.value.push({
    role: 'user',
    text: userMessage,
  })

  await scrollToBottom()

  message.value = ''
  loading.value = true
  error.value = ''

  try {
    const response = await api.get('/api/ai/chat', {
      params: {
        message: userMessage,
      },
    })

    await scrollToBottom()

    messages.value.push({
      role: 'assistant',
      text: response.data,
    })
  } catch (err) {
    console.error(err)
    error.value = 'Could not contact the assistant'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <button class="chat-toggle" type="button" @click="toggleChat">
    <span v-if="isOpen"> ✕ </span>

    <img v-else src="@/assets/ai_boot.svg" alt="AI Assistant" />
  </button>

  <div v-if="isOpen" class="chat-panel">
    <div class="chat-header">
      <span>Rugby Assistant</span>
    </div>

    <div ref="chatBody" class="chat-body">
      <div v-for="(item, index) in messages" :key="index" class="message-row" :class="item.role">
        <div class="avatar">
          {{ item.role === 'user' ? '🏉' : '🤖' }}
        </div>

        <div class="chat-message" :class="item.role">
          {{ item.text }}
        </div>
      </div>
      <p v-if="loading" class="loading-message">Assistant is thinking...</p>

      <p v-if="error" class="chat-error">
        {{ error }}
      </p>
      <p v-if="!messages.length" class="empty-chat">
        Ask me about standings, matches, teams or tournament rules.
      </p>
    </div>

    <form class="chat-form" @submit.prevent="sendMessage">
      <input v-model="message" type="text" placeholder="Ask something..." />

      <button type="submit">Send</button>
    </form>
  </div>
</template>

<style scoped>
.chat-toggle {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 80;
  width: 60px;
  height: 60px;
  border: 1px solid;
  border-radius: 50%;
  background: var(--primary-light);
  color: white;
  font-size: 28px;
  box-shadow: var(--shadow);
}

.chat-toggle {
  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease;
}

.chat-toggle:hover {
  transform: scale(1.08);
  overflow: visible;
}

.chat-toggle img {
  width: 45px;
  height: 45px;
}

.chat-toggle::after {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 50%;
  border: 2px solid rgba(18, 60, 105, 0.3);
  animation: pulse 2s infinite;
  pointer-events: none;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.6;
  }

  100% {
    transform: scale(1.8);
    opacity: 0;
  }
}

.chat-panel {
  position: fixed;
  right: 20px;
  bottom: 90px;
  width: 340px;
  height: 500px;
  background: rgb(242, 244, 247);
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  z-index: 79;
}

.chat-header {
  background: var(--primary-light);
  color: white;
  padding: 16px;
  font-weight: 800;
}

.chat-body {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  color: var(--text-secondary);
}

.message-row {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.message-row.user {
  justify-content: flex-end;
}

.message-row.assistant {
  justify-content: flex-start;
}

.message-row.user .avatar {
  order: 2;
}

.avatar {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: #eef2f7;
  font-size: 16px;
  flex-shrink: 0;
}

.chat-message {
  max-width: 75%;
  padding: 10px 12px;
  border-radius: 14px;
  line-height: 1.4;
  font-size: 14px;
}

.chat-message.user {
  background: var(--primary);
  color: white;
  border-bottom-right-radius: 4px;
}

.chat-message.assistant {
  background: #f1f3f5;
  color: var(--text-primary);
  border-bottom-left-radius: 4px;
}

.empty-chat,
.loading-message,
.chat-error {
  font-size: 14px;
  color: var(--text-secondary);
}

.chat-error {
  color: #c62828;
}

.chat-form {
  display: flex;
  gap: 8px;
  padding: 12px;
  border-top: 1px solid var(--border);
  background: white;
}

.chat-form input {
  flex: 1;
  border: 1px solid var(--border);
  border-radius: 999px;
  padding: 10px 14px;
  font-size: 14px;
  outline: none;
}

.chat-form input:focus {
  border-color: var(--primary);
}

.chat-form button {
  border: none;
  border-radius: 999px;
  padding: 10px 14px;
  background: var(--primary-light);
  color: white;
  font-weight: 800;
  cursor: pointer;
}
.chat-form button:hover{
  background: var(--primary);
}
</style>

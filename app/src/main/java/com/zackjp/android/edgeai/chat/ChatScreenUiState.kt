package com.zackjp.android.edgeai.chat

import androidx.compose.runtime.Immutable

@Immutable
data class ChatScreenUiState(
    val messages: List<ChatMessage>
)

@Immutable
data class ChatMessage(
    val chatId: String,
    val author: ChatAuthor,
    val text: String,
)

enum class ChatAuthor {
    Agent,
    User,
}

package com.zackjp.android.edgeai.chat

import androidx.a2ui.model.processor.A2uiSurfaceModel
import androidx.compose.runtime.Immutable

@Immutable
data class ChatScreenUiState(
    val messages: List<ChatMessage>
)

@Immutable
sealed interface ChatMessage {
    val chatId: String
}

@Immutable
data class UserMessage(
    override val chatId: String,
    val text: String,
) : ChatMessage

@Immutable
data class AgentMessage(
    override val chatId: String,
    val text: String,
    val surface: A2uiSurfaceModel? = null,
) : ChatMessage

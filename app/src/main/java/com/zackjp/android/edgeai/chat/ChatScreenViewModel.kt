package com.zackjp.android.edgeai.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        ChatScreenUiState(
            messages = emptyList(),
        )
    )

    val state = _state.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    fun onUserMessage(text: String) {
        val echoText = getAgentEchoText(text)

        _state.update {
            val newMessages = listOfNotNull(
                ChatMessage(
                    chatId = generateChatId(),
                    author = ChatAuthor.User,
                    text = text,
                ),
                echoText?.let { echo ->
                    ChatMessage(
                        chatId = generateChatId(),
                        author = ChatAuthor.Agent,
                        text = echo,
                    )
                }
            )

            with(it) {
                copy(
                    messages = messages + newMessages,
                )
            }
        }
    }

}

@OptIn(ExperimentalUuidApi::class)
private fun generateChatId() = Uuid.generateV7().toString()

private fun getAgentEchoText(text: String): String? =
    if (text.startsWith(prefix = "say ", ignoreCase = true)) {
        val echo = text.substring(4)
        echo.ifBlank { null }
    } else {
        null
    }

package com.zackjp.android.edgeai.chat.data

import androidx.a2ui.model.processor.A2uiSurfaceModel
import androidx.a2ui.model.protocol.A2uiClientErrorMessage
import androidx.a2ui.model.protocol.A2uiClientEventMessage
import androidx.a2ui.model.protocol.A2uiClientToServerMessage
import com.zackjp.android.edgeai.chat.AgentMessage
import com.zackjp.android.edgeai.chat.ChatMessage
import com.zackjp.android.edgeai.chat.ChatScreenUiState
import com.zackjp.android.edgeai.chat.UserMessage
import com.zackjp.android.edgeai.chat.domain.ChatAgent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatSession(
    private val agent: ChatAgent,
    private val scope: CoroutineScope,
    onA2uiMessage: (String) -> Unit,
    surfaces: StateFlow<List<A2uiSurfaceModel>>
) {

    private val _state = MutableStateFlow(
        ChatScreenUiState(
            messages = emptyList(),
        )
    )

    val uiState get() = _state.asStateFlow()

    init {
        scope.launch(Dispatchers.Default) {
            agent.response().collect {
                onA2uiMessage(it)
            }
        }

        scope.launch(Dispatchers.Default) {
            surfaces.collect { surfaceList ->
                if (surfaceList.isEmpty()) return@collect

                _state.update {
                    with(it) {
                        val processedMessages = processNewAndRemovedSurfaces(
                            messages = messages,
                            activeSurfaces = surfaceList,
                        )
                        val filteredMessages = processedMessages.first
                        val newSurfaces = processedMessages.second

                        val newAgentMessages = newSurfaces.map { newSurface ->
                            AgentMessage(
                                chatId = generateChatId(),
                                text = "Agent response:",
                                surface = newSurface,
                            )
                        }

                        if (newAgentMessages.isEmpty()) {
                            // no change
                            it
                        } else {
                            copy(
                                messages = filteredMessages + newAgentMessages,
                            )
                        }
                    }
                }
            }
        }
    }

    fun sendPrompt(text: String) {
        _state.update {
            with(it) {
                copy(
                    messages = messages + UserMessage(
                        chatId = generateChatId(),
                        text = text,
                    )
                )
            }
        }

        scope.launch {
            agent.sendMessage(text)
        }
    }

    fun handleOutboundA2uiEvent(event: A2uiClientToServerMessage) {
        when (event) {
            is A2uiClientEventMessage -> {
                event.toString()
            }

            is A2uiClientErrorMessage -> {}
        }
    }

    private fun processNewAndRemovedSurfaces(
        messages: List<ChatMessage>,
        activeSurfaces: List<A2uiSurfaceModel>,
    ): Pair<List<ChatMessage>, List<A2uiSurfaceModel>> {
        // preallocate for performance
        val retainedChatMessages: MutableList<ChatMessage> = ArrayList(messages.size)
        // starts with full list and removes ones currently in use. remaining ones are new
        val newSurfaces = activeSurfaces.toMutableList()

        messages.forEach { chatMessage ->
            if (chatMessage !is AgentMessage) {
                retainedChatMessages.add(chatMessage)
                return@forEach
            }

            if (chatMessage.surface == null) {
                retainedChatMessages.add(chatMessage)
                return@forEach
            }

            // if any surfaces were found, then the ChatMessage surface is still active
            val isSurfaceActive = newSurfaces.removeAll { it.id == chatMessage.surface.id }
            if (isSurfaceActive) {
                retainedChatMessages.add(chatMessage)
            }
        }

        return Pair(retainedChatMessages, newSurfaces)
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

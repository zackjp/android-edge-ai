package com.zackjp.android.edgeai.chat

import android.app.Application
import androidx.a2ui.compose.runtime.A2uiMessageParser
import androidx.a2ui.compose.ui.A2uiMessageProcessor
import androidx.a2ui.model.processor.processInput
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zackjp.android.edgeai.chat.a2ui.createCatalog
import com.zackjp.android.edgeai.chat.data.ChatSession
import com.zackjp.android.edgeai.chat.data.FakeAgent
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

class ChatScreenViewModel(
    application: Application,
) : ViewModel() {

    private val catalog = createCatalog(application)
    private val processor = A2uiMessageProcessor(catalogs = listOf(catalog))
    private val parser = A2uiMessageParser()

    private val chat =
        ChatSession(
            agent = FakeAgent(),
            scope = viewModelScope,
            onA2uiMessage = { processor.processInput(parser, it) },
            surfaces = processor.activeSurfaces,
        )

    val state = chat.uiState

    init {
        viewModelScope.launch(Dispatchers.Default) {
            processor.collectMessages()
        }

        viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            processor.outboundEvents.collect(chat::handleOutboundA2uiEvent)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onUserMessage(text: String) {
        chat.sendPrompt(text)
    }

}

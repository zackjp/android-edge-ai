package com.zackjp.android.edgeai.chat.domain

import kotlinx.coroutines.flow.Flow

interface ChatAgent {

    fun response(): Flow<String>

    suspend fun sendMessage(userPrompt: String)

}

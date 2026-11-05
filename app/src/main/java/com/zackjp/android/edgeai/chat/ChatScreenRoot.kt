package com.zackjp.android.edgeai.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zackjp.android.edgeai.R


private object ChatTokens {
    val UserBackground = Color(0xffdae2cd)
    val UserTextColor = Color(0xff232421)
    val SidePadding = 48.dp
    val ItemSpacing = 4.dp
}

@Composable
fun ChatScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ChatScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChatScreen(
        modifier = modifier,
        state = state,
        onUserMessage = viewModel::onUserMessage,
    )
}

@Composable
private fun ChatScreen(
    modifier: Modifier = Modifier,
    state: ChatScreenUiState,
    onUserMessage: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        ChatMessageList(
            modifier = Modifier.weight(1f),
            messages = state.messages,
        )

        UserMessageInput(
            modifier = Modifier.fillMaxWidth(),
            onUserMessage = onUserMessage,
        )
    }
}

@Composable
fun ChatMessageList(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
) {
    val lazyListState = rememberLazyListState()
    val latestMessageId = remember(messages) {
        messages.lastOrNull()?.chatId
    }
    LaunchedEffect(latestMessageId) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.lastIndex)
        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 32.dp),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(
            space = ChatTokens.ItemSpacing,
            alignment = Alignment.Bottom,
        ),
    ) {
        items(
            items = messages,
            key = { it.chatId },
        ) { message ->
            when (message.author) {
                ChatAuthor.Agent -> AgentMessage(
                    modifier = Modifier.fillMaxWidth(),
                    text = message.text,
                )
                ChatAuthor.User -> UserMessage(
                    modifier = Modifier.fillMaxWidth(),
                    text = message.text,
                )
            }
        }
    }
}

@Composable
fun AgentMessage(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(end = ChatTokens.SidePadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = text,
            )
        }
    }
}

@Composable
fun UserMessage(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        Box(
            modifier = Modifier
                .padding(start = ChatTokens.SidePadding)
                .clip(MaterialTheme.shapes.large)
                .background(ChatTokens.UserBackground)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                color = ChatTokens.UserTextColor,
                style = MaterialTheme.typography.bodyLarge,
                text = text,
            )
        }
    }
}

@Composable
private fun UserMessageInput(
    modifier: Modifier = Modifier,
    onUserMessage: (String) -> Unit,
) {
    val textFieldState = rememberTextFieldState()
    OutlinedTextField(
        modifier = modifier,
        placeholder = {
            Text(stringResource(R.string.chat_screen_chat_placeholder))
        },
        shape = MaterialTheme.shapes.extraLarge,
        state = textFieldState,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send,
        ),
        onKeyboardAction = {
            val userText = textFieldState.text.toString()
            textFieldState.clearText()
            onUserMessage(userText)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreen(
        modifier = Modifier
            .fillMaxSize(),
        state = ChatScreenUiState(
            messages = listOf(
                ChatMessage(
                    chatId = "user-111",
                    author = ChatAuthor.User,
                    text = "Short user text",
                ),
                ChatMessage(
                    chatId = "agent-111",
                    author = ChatAuthor.Agent,
                    text = "Short agent text",
                ),
                ChatMessage(
                    chatId = "user-222",
                    author = ChatAuthor.User,
                    text = "Long user text should be placed here so that the boundaries can be fully tested",
                ),
                ChatMessage(
                    chatId = "agent-222",
                    author = ChatAuthor.Agent,
                    text = "Long agent text should be placed here so that the boundaries can be fully tested",
                ),
            ),
        ),
        onUserMessage = {},
    )
}

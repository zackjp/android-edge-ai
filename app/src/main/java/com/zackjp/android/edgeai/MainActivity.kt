package com.zackjp.android.edgeai

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zackjp.android.edgeai.chat.ChatScreenRoot
import com.zackjp.android.edgeai.chat.ChatScreenViewModel
import com.zackjp.android.edgeai.ui.theme.AndroidEdgeAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidEdgeAITheme {
                AppContent()
            }
        }
    }

}

@Composable
private fun AppContent() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        val appContext = LocalContext.current.applicationContext
        val chatScreenVMFactory = remember(appContext) {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return ChatScreenViewModel(
                        application = appContext as Application,
                    ) as T
                }
            }
        }

        val viewModel: ChatScreenViewModel = viewModel(factory = chatScreenVMFactory)

        ChatScreenRoot(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
            viewModel = viewModel,
        )
    }
}

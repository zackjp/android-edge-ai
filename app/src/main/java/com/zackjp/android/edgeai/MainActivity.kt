package com.zackjp.android.edgeai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zackjp.android.edgeai.describeimage.DescribeImageScreenRoot
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
        DescribeImageScreenRoot(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

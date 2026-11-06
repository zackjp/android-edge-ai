package com.zackjp.android.edgeai.chat.a2ui

import android.content.Context
import androidx.a2ui.compose.ui.A2uiCatalog
import androidx.a2ui.compose.ui.catalog.A2uiBasicCatalogV1
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.material3.a2ui.catalog.A2uiAudioPlayerRenderer
import androidx.compose.material3.a2ui.catalog.A2uiImageRenderer
import androidx.compose.material3.a2ui.catalog.A2uiVideoRenderer
import androidx.compose.material3.a2ui.catalog.MaterialA2uiBasicCatalogV1Defaults
import androidx.compose.material3.a2ui.catalog.materialA2uiBasicCatalogV1
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage


fun createCatalog(context: Context): A2uiCatalog {
    return materialA2uiBasicCatalogV1(
        image = MaterialA2uiBasicCatalogV1Defaults.image(CoilA2uiImageRenderer),
        video = MaterialA2uiBasicCatalogV1Defaults.video(EmptyA2uiVideoRenderer),
        audioPlayer = MaterialA2uiBasicCatalogV1Defaults.audioPlayer(EmptyA2uiAudioPlayer),
        functions = A2uiBasicCatalogV1.Functions(context),
    )
}

private object CoilA2uiImageRenderer : A2uiImageRenderer {
    @Composable
    override fun Image(
        url: String,
        contentDescription: String?,
        contentScale: ContentScale,
        modifier: Modifier,
        onError: (throwable: Throwable?) -> Unit,
    ) {
        AsyncImage(
            modifier = modifier,
            contentDescription = contentDescription,
            contentScale = contentScale,
            model = url,
            onError = { onError(it.result.throwable) }
        )
    }

}

private object EmptyA2uiVideoRenderer : A2uiVideoRenderer {
    @Composable
    override fun Video(
        url: String,
        modifier: Modifier,
        onError: (throwable: Throwable?) -> Unit,
    ) {
        Box(
           modifier = modifier,
        ) {
            Text(
                text = "Video renderer: $url",
            )
        }
    }

}

private object EmptyA2uiAudioPlayer : A2uiAudioPlayerRenderer {
    @Composable
    override fun AudioPlayer(
        url: String,
        contentDescription: String?,
        modifier: Modifier,
        onError: (throwable: Throwable?) -> Unit
    ) {
        Box(
            modifier = modifier,
        ) {
            Text(
                text = "Audio: $url",
            )
        }
    }

}

package com.zackjp.android.edgeai.describeimage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.zackjp.android.edgeai.R
import com.zackjp.android.edgeai.ui.theme.AndroidEdgeAITheme


@Composable
fun DescribeImageScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: DescribeImageViewModel = viewModel<DescribeImageViewModel>(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier,
    ) {
        DescribeImageScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            selectedImageUri = uiState.imageUri,
            onImageUriSelected = viewModel::onImageUriSelected,
        )
    }
}

@Composable
private fun DescribeImageScreen(
    modifier: Modifier = Modifier,
    selectedImageUri: Uri?,
    onImageUriSelected: (imageUri: Uri?) -> Unit
) {

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { imageUri ->
        onImageUriSelected(imageUri)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        selectedImageUri?.let { uri ->
            Card(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .aspectRatio(2f),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    model = uri,
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(stringResource(R.string.describe_image_select_image_label))

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                selectImageLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly,
                    )
                )
            },
        ) {
            Text(stringResource(R.string.describe_image_browse_images))
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDescribeImageScreen() {
    AndroidEdgeAITheme {
        DescribeImageScreen(
            modifier = Modifier.fillMaxSize(),
            selectedImageUri = null,
            onImageUriSelected = {},
        )
    }
}

package com.zackjp.android.edgeai.describeimage

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DescribeImageViewModel: ViewModel() {

    private val _state = MutableStateFlow(
        DescribeImageUiState(),
    )
    val state: StateFlow<DescribeImageUiState> = _state.asStateFlow()

    fun onImageUriSelected(uri: Uri?) {
        _state.update {
            it.copy(imageUri = uri)
        }
    }

}

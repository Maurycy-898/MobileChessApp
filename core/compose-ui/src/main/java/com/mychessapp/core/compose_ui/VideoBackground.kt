package com.mychessapp.core.compose_ui

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

@Composable
fun VideoBackground(
  videoUri: Uri,
  modifier: Modifier = Modifier,
  contentAlignment: Alignment = Alignment.Center,
  content: @Composable () -> Unit,
) {
  Box(
    modifier = modifier,
    contentAlignment = contentAlignment
  ) {
    Box(Modifier.blur(50.dp)) {
      Video(
        modifier = Modifier.fillMaxSize(),
        videoUri = videoUri,
      )
    }
    content.invoke()
  }
}

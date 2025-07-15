package com.mychessapp.core.compose_ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoadingContent(modifier: Modifier = Modifier.fillMaxSize()) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator()
  }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
  Surface {
    LoadingContent()
  }
}

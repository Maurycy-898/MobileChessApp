package com.mychessapp.core.compose_ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mychessapp.core.compose_ui.R

@Composable
fun ErrorContent(
  modifier: Modifier = Modifier.fillMaxSize(),
  onRetry: () -> Unit = {}
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(IntrinsicSize.Max)
    ) {
      Text(
        text = stringResource(R.string.unexpected_error),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Light,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onRetry
      ) {
        Text(text = stringResource(R.string.try_again))
      }
    }
  }
}

@Preview
@Composable
private fun ErrorContentPreview() {
  Surface {
    ErrorContent()
  }
}

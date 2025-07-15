package com.mychessapp.screen.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mychessapp.core.compose_ui.VideoBackground
import com.mychessapp.core.compose_ui.videoResourceUri
import com.mychessapp.screen.login.internal.Login
import com.mychessapp.screen.login.internal.LoginState
import com.mychessapp.screen.login.internal.LoginViewAction
import com.mychessapp.screen.login.internal.LoginViewModel

@Composable
fun LoginScreen(
  modifier: Modifier = Modifier,
) {
  val viewModel = viewModel<LoginViewModel>()
  val state by viewModel.state.collectAsState()

  Scaffold { contentPadding ->
    LoginScreenLayout(
      state = state,
      onViewAction = viewModel::onViewAction,
      modifier = modifier
        .fillMaxSize()
        .padding(contentPadding),
    )
  }
}

@Composable
private fun LoginScreenLayout(
  state: LoginState,
  modifier: Modifier = Modifier,
  onViewAction: (LoginViewAction) -> Unit = {},
) {
  VideoBackground(
    modifier = modifier,
    videoUri = videoResourceUri(R.raw.chess_bg)
  ) {
    Login(
      modifier = Modifier.padding(24.dp),
      loginState = state,
      onViewAction = onViewAction,
    )
  }
}

@Preview
@Composable
private fun LoginScreenPreview() {
  Surface {
    LoginScreenLayout(
      modifier = Modifier.fillMaxSize(),
      state = LoginState()
    )
  }
}

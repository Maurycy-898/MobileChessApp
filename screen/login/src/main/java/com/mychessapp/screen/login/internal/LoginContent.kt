package com.mychessapp.screen.login.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mychessapp.core.compose_ui.screens.ErrorContent
import com.mychessapp.core.compose_ui.screens.LoadingContent
import com.mychessapp.repository_model.Password
import com.mychessapp.repository_model.UserName
import com.mychessapp.screen.login.R

@Composable
internal fun Login(
  modifier: Modifier = Modifier,
  loginState: LoginState,
  onViewAction: (LoginViewAction) -> Unit,
) {
  Card(
    modifier = modifier,
    backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.92f),
    shape = RoundedCornerShape(16.dp),
    elevation = 8.dp,
  ) {
    when {
      loginState.isLoading -> LoadingContent()
      loginState.errorMessage.isNotBlank() -> ErrorContent()
      else -> {
        LoginContent(
          modifier = Modifier.padding(24.dp),
          loginState = loginState,
          onViewAction = onViewAction,
        )
      }
    }
  }
}

@Composable
private fun LoginContent(
  modifier: Modifier = Modifier,
  loginState: LoginState,
  onViewAction: (LoginViewAction) -> Unit,
) {
  Layout(
    modifier = modifier,
    usernameTextField = {
      UsernameTextField(
        username = loginState.username,
        onUsernameChanged = { onViewAction(it) },
      )
    },
    passwordTextField = {
      PasswordTextField(
        password = loginState.password,
        onPasswordChanged = { onViewAction(it) },
      )
    },
    loginButton = {
      LoginButton(
        isEnabled = loginState.isLoginButtonEnabled,
        onClick = { onViewAction(LoginViewAction.OnLoginClicked) },
      )
    }
  )
}

@Composable
private fun Layout(
  modifier: Modifier = Modifier,
  usernameTextField: @Composable () -> Unit,
  passwordTextField: @Composable () -> Unit,
  loginButton: @Composable () -> Unit,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Title()
    usernameTextField()
    passwordTextField()
    loginButton()
  }
}

@Composable
private fun Title() {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.height(IntrinsicSize.Max),
  ) {
    Icon(
      modifier = Modifier.size(52.dp),
      imageVector = Icons.Rounded.AccountCircle,
      contentDescription = null,
    )
    Spacer(Modifier.height(8.dp))
    Text(
      text = stringResource(R.string.sign_in),
      style = MaterialTheme.typography.h4,
    )
  }
}

@Composable
private fun UsernameTextField(
  username: UserName,
  modifier: Modifier = Modifier,
  onUsernameChanged: (LoginViewAction.OnUsernameChanged) -> Unit,
) {
  TextField(
    value = username.value,
    onValueChange = { onUsernameChanged(LoginViewAction.OnUsernameChanged(it)) },
    label = { Text(text = stringResource(R.string.username)) },
    modifier = modifier,
  )
}

@Composable
private fun PasswordTextField(
  password: Password,
  modifier: Modifier = Modifier,
  onPasswordChanged: (LoginViewAction.OnPasswordChanged) -> Unit,
) {
  TextField(
    value = password.value,
    onValueChange = { onPasswordChanged(LoginViewAction.OnPasswordChanged(it)) },
    label = { Text(text = stringResource(R.string.password)) },
    modifier = modifier,
  )
}

@Composable
private fun LoginButton(
  isEnabled: Boolean,
  onClick: () -> Unit,
) {
  Button(
    enabled = isEnabled,
    onClick = onClick
  ) {
    Text(
      modifier = Modifier.fillMaxWidth(.42f),
      textAlign = TextAlign.Center,
      text = stringResource(R.string.sign_in),
    )
  }
}

@Preview
@Composable
private fun LoginPreview() {
  Surface {
    Login(
      modifier = Modifier.padding(16.dp),
      loginState = LoginState(),
      onViewAction = {},
    )
  }
}

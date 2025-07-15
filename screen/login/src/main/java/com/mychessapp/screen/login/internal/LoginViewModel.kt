package com.mychessapp.screen.login.internal

import androidx.lifecycle.ViewModel
import com.mychessapp.repository_model.Password
import com.mychessapp.repository_model.UserName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor() : ViewModel() {

  private val _state = MutableStateFlow(LoginState())
  val state: StateFlow<LoginState> = _state.asStateFlow()

  fun onViewAction(action: LoginViewAction) {
    when (action) {
      is LoginViewAction.OnUsernameChanged -> {
        _state.update { it.copy(username = UserName(action.newUsername)) }
      }
      is LoginViewAction.OnPasswordChanged -> {
        _state.update{ it.copy(password = Password(action.newPassword)) }
      }
      is LoginViewAction.OnLoginClicked -> login()
    }
  }

  private fun login() {
    _state.value = _state.value.copy(isLoading = true)
    // TODO: Perform login
    _state.value = _state.value.copy(isLoading = false)
  }
}
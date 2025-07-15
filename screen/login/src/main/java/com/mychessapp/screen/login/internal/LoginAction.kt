package com.mychessapp.screen.login.internal

sealed interface LoginViewAction {
  data class OnUsernameChanged(val newUsername: String) : LoginViewAction
  data class OnPasswordChanged(val newPassword: String) : LoginViewAction
  data object OnLoginClicked : LoginViewAction
}
package com.mychessapp.screen.login.internal

import com.mychessapp.repository_model.Password
import com.mychessapp.repository_model.UserName

data class LoginState(
  val username: UserName = UserName(""),
  val password: Password = Password(""),
  val isLoginButtonEnabled: Boolean = false,
  val isLoading: Boolean = false,
  val errorMessage: String = "",
)

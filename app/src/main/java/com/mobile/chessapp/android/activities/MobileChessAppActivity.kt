package com.mobile.chessapp.android.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.example.screen.home.HomeScreen
import com.mychessapp.screen.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileChessAppActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        enableEdgeToEdge()
        LoginScreen()
//        HomeScreen(
//          modifier = Modifier
//            .fillMaxSize()
//            .imePadding()
//        )
      }
    }
  }
}



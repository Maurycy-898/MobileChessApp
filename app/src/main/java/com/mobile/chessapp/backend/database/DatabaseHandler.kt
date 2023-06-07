package com.mobile.chessapp.backend.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object DatabaseHandler {
    val database = Firebase.database
}
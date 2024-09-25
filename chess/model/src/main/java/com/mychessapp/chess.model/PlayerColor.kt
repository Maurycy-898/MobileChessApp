package com.mychessapp.chess.model

enum class PlayerColor {
  White,
  Black,
}

fun PlayerColor.oppositeColor() =
  when (this) {
    PlayerColor.White -> PlayerColor.Black
    PlayerColor.Black -> PlayerColor.White
  }

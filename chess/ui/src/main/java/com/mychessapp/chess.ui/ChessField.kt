package com.mychessapp.chess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.ui.model.ChessFieldColor
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.ui.model.ChessFieldMark
import com.mychessapp.chess.ui.model.ChessFieldUiState
import com.mychessapp.chess.ui.model.color
import com.mychessapp.chess.ui.preview.Preview.exampleChessField

private object ChessFieldColors {
  val dark = Color.DarkGray
  val light = Color.LightGray
}

@Composable
fun ChessField(
  chessFieldState: ChessFieldUiState,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .background(color = chessFieldState.field.color.asColor())
      .fieldBackground(chessFieldState.field.color),
    contentAlignment = Alignment.Center
  ) {
    when (chessFieldState.field) {
      is ChessField.Empty -> Unit
      is ChessField.WithPiece -> ChessPiece(chessFieldState.field.piece)
    }
  }
}

private fun Modifier.fieldBackground(color: ChessFieldColor) =
  this.background(color = color.asColor(), shape = RectangleShape)

private fun ChessFieldColor.asColor() =
  when (this) {
    ChessFieldColor.Dark -> ChessFieldColors.dark
    ChessFieldColor.Light -> ChessFieldColors.light
  }

private class ChessFieldPreviewParameter : CollectionPreviewParameterProvider<ChessField>(
  with(exampleChessField) {
    listOf(
      this,
      ChessField.Empty(position = ChessFieldPosition(row = 0, column = 2)),
      ChessField.Empty(position = ChessFieldPosition(row = 0, column = 3))
    )
  }
)

@Preview
@Composable
private fun ChessFieldPreview(
  @PreviewParameter(ChessFieldPreviewParameter::class)
  chessField: ChessField
) {
  MaterialTheme {
    Surface {
      ChessField(
        chessFieldState = ChessFieldUiState(chessField, ChessFieldMark.None),
        modifier = Modifier.size(64.dp)
      )
    }
  }
}

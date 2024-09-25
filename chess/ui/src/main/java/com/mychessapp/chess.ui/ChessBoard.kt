package com.mychessapp.chess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.ui.model.ChessBoardUiState
import com.mychessapp.chess.ui.model.ChessFieldUiState
import com.mychessapp.chess.ui.model.position
import com.mychessapp.chess.ui.preview.Preview.exampleChessBoard
import com.mychessapp.core.common.collections.matrix.forEachRow

private val boardBackground = Color.DarkGray

@Composable
fun ChessBoard(
  board: ChessBoardUiState,
  modifier: Modifier = Modifier,
  onChessFieldClick: (ChessFieldPosition) -> Unit = {}
) {
  Column(modifier.boardOutline()) {
    board.forEachRow { fieldsRow ->
      FieldsRow(
        fields = fieldsRow,
        onChessFieldClick = onChessFieldClick
      )
    }
  }
}

@Composable
private fun FieldsRow(
  fields: List<ChessFieldUiState>,
  modifier: Modifier = Modifier,
  onChessFieldClick: (ChessFieldPosition) -> Unit = {}
) {
  Row(modifier) {
    fields.forEach { field ->
      ChessField(
        chessFieldState = field,
        modifier = Modifier
          .aspectRatio(1f)
          .weight(1f)
          .clickable { onChessFieldClick(field.position) }
      )
    }
  }
}

private fun Modifier.boardOutline() = this
  .wrapContentSize()
  .background(boardBackground)
  .padding(4.dp)

private class ChessBoardPreviewParameter : CollectionPreviewParameterProvider<ChessBoardUiState>(
  with(exampleChessBoard) {
    listOf(
      this,
    )
  }
)

@Preview(heightDp = 1000, widthDp = 500)
@Composable
private fun ChessBoardPreview(
  @PreviewParameter(ChessBoardPreviewParameter::class)
  chessBoard: ChessBoardUiState
) {
  MaterialTheme {
    Surface {
      ChessBoard(
        board = chessBoard,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

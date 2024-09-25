package com.mychessapp.chess.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.mychessapp.chess.model.ChessPiece
import com.mychessapp.chess.model.PlayerColor
import com.mychessapp.chess.model.ChessPieceType
import com.mychessapp.chess.ui.preview.Preview.exampleChessPiece

@Composable
fun ChessPiece(
  piece: ChessPiece,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Image(
      painterResource(piece.getResourceId()),
      contentDescription = null
    )
  }
}

private fun ChessPiece.getResourceId() =
  when (color) {
    PlayerColor.White -> type.getWhitePieceResourceId()
    PlayerColor.Black -> type.getBlackPieceResourceId()
  }

private fun ChessPieceType.getWhitePieceResourceId() =
  when (this) {
    ChessPieceType.Pawn -> R.drawable.pawn_white
    ChessPieceType.Knight -> R.drawable.knight_white
    ChessPieceType.Bishop -> R.drawable.bishop_white
    ChessPieceType.Rook -> R.drawable.rook_white
    ChessPieceType.Queen -> R.drawable.queen_white
    ChessPieceType.King -> R.drawable.king_white
  }

private fun ChessPieceType.getBlackPieceResourceId() =
  when (this) {
    ChessPieceType.Pawn -> R.drawable.pawn_black
    ChessPieceType.Knight -> R.drawable.knight_black
    ChessPieceType.Bishop -> R.drawable.bishop_black
    ChessPieceType.Rook -> R.drawable.rook_black
    ChessPieceType.Queen -> R.drawable.queen_black
    ChessPieceType.King -> R.drawable.king_black
  }

private class ChessPiecePreviewParameter : CollectionPreviewParameterProvider<ChessPiece>(
  with(exampleChessPiece) {
    listOf(
      this,
      copy(color = PlayerColor.White),
      copy(type = ChessPieceType.Pawn),
      copy(type = ChessPieceType.Knight),
      copy(type = ChessPieceType.Bishop),
      copy(type = ChessPieceType.Rook),
      copy(type = ChessPieceType.Queen),
    )
  }
)

@Preview
@Composable
private fun ChessPiecePreview(
  @PreviewParameter(ChessPiecePreviewParameter::class)
  chessPiece: ChessPiece
) {
  MaterialTheme {
    Surface {
      ChessPiece(chessPiece)
    }
  }
}

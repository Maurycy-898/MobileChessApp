package com.mychessapp.chess.model

import com.mychessapp.core.common.collections.matrix.MutableMatrix

/**
 * Representation of the chess board state
 * @param fields fields of this board where fields.get(a)(b) -> a: row, b: column
 *
 * @throws IllegalStateException there must be at least one row and number of rows
 * must be equal to number of columns
 *
 */
class ChessBoard private constructor(
  private val fields: MutableMatrix<ChessField>
) : MutableMatrix<ChessField> by fields {

  operator fun get(chessFieldPosition: ChessFieldPosition) =
    fields[chessFieldPosition.row, chessFieldPosition.column]

  operator fun set(chessFieldPosition: ChessFieldPosition, newField: ChessField) {
    fields[chessFieldPosition.row, chessFieldPosition.column] = newField
  }

  fun ChessField.isOnChessBoard(): Boolean = position.isOnChessBoard()

  fun ChessFieldPosition.isOnChessBoard(): Boolean =
    row in 0..<rowsSize && column in 0..<columnsSize

  fun ChessFieldPosition.isEmptyField() = get(this).isEmptyField()

  fun ChessFieldPosition.isFieldWithPiece() = get(this).isFieldWithPiece()

  private fun ChessField.isEmptyField(): Boolean = this is ChessField.Empty

  private fun ChessField.isFieldWithPiece(): Boolean = this is ChessField.Empty

  companion object {

    private const val DefaultChessBoardSize = 8

    fun createEmptyBoard(size: Int = DefaultChessBoardSize) =
      MutableMatrix(size) { row, column ->
        ChessFieldPosition(row, column).let(ChessField::Empty)
      } as ChessBoard
  }
}

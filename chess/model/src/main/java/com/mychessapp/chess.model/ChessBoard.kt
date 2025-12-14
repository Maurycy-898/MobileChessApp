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
@Suppress("Unused")
class ChessBoard private constructor(
  private val fields: MutableMatrix<ChessField>,
) : MutableMatrix<ChessField> by fields {

  operator fun get(position: ChessFieldPosition): ChessField =
    fields[position.row, position.column]

  fun getOrNull(position: ChessFieldPosition): ChessField? =
    if (isOnChessBoard(position)) fields[position.row, position.column] else null

  operator fun set(chessFieldPosition: ChessFieldPosition, newField: ChessField) {
    fields[chessFieldPosition.row, chessFieldPosition.column] = newField
  }

  fun isOnChessBoard(field: ChessField): Boolean = isOnChessBoard(field.position)

  fun isOnChessBoard(position: ChessFieldPosition): Boolean =
    position.row in 0..<rowsSize && position.column in 0..<columnsSize

  fun ChessFieldPosition.isEmptyField() = get(this).isEmptyField()

  fun ChessFieldPosition.isNotEmptyField() = get(this).isNotEmptyField()

  fun ChessFieldPosition.isFieldWithPiece() = get(this).isFieldWithPiece()

  private fun ChessField.isEmptyField(): Boolean = this is ChessField.Empty

  private fun ChessField.isNotEmptyField(): Boolean = !isEmptyField()

  private fun ChessField.isFieldWithPiece(): Boolean = this is ChessField.Empty

  companion object {

    private const val DefaultChessBoardSize = 8

    fun empty(size: Int = DefaultChessBoardSize) =
      MutableMatrix<ChessField>(size) { row, column ->
        ChessFieldPosition(row, column).let(ChessField::Empty)
      }.let(::ChessBoard)
  }
}

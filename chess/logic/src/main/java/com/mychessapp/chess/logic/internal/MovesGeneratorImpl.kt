package com.mychessapp.chess.logic.internal

import com.mychessapp.chess.logic.MovesGenerator
import com.mychessapp.chess.logic.internal.model.GameMetadata
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.move_generators.BishopMovesGenerator
import com.mychessapp.chess.logic.internal.move_generators.KingMovesGenerator
import com.mychessapp.chess.logic.internal.move_generators.KnightMovesGenerator
import com.mychessapp.chess.logic.internal.move_generators.PawnMovesGenerator
import com.mychessapp.chess.logic.internal.move_generators.QueenMovesGenerator
import com.mychessapp.chess.logic.internal.move_generators.RookMovesGenerator
import com.mychessapp.chess.model.ChessBoard
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessPieceType
import javax.inject.Inject

internal class MovesGeneratorImpl @Inject constructor(
  private val pawnMovesGenerator: PawnMovesGenerator,
  private val knightMovesGenerator: KnightMovesGenerator,
  private val bishopMovesGenerator: BishopMovesGenerator,
  private val rookMovesGenerator: RookMovesGenerator,
  private val queenMovesGenerator: QueenMovesGenerator,
  private val kingMovesGenerator: KingMovesGenerator,
) : MovesGenerator {

  override fun generateAllLegalMoves(
    board: ChessBoard,
    metadata: GameMetadata,
  ) = board.fieldsList()
    .flatMap { field ->
      generateAllLegalMovesFromField(field, board, metadata)
    }

  override fun generateAllLegalMovesFromField(
    field: ChessField,
    board: ChessBoard,
    metadata: GameMetadata,
  ) = when (field) {
    is ChessField.Empty -> emptyList()
    is ChessField.WithPiece -> generatePieceMoves(board, metadata, field)
  }

  private fun generatePieceMoves(
    board: ChessBoard,
    metadata: GameMetadata,
    field: ChessField.WithPiece,
  ) = createMoveGeneratorContext(
    board = board,
    metadata = metadata,
    fromField = field
  ).generatePieceMoves()

  private fun MoveGeneratorContext.generatePieceMoves() = when (fromField.piece.type) {
    ChessPieceType.Pawn -> pawnMovesGenerator.generate(this)
    ChessPieceType.Knight -> knightMovesGenerator.generate(this)
    ChessPieceType.Bishop -> bishopMovesGenerator.generate(this)
    ChessPieceType.Rook -> rookMovesGenerator.generate(this)
    ChessPieceType.Queen -> queenMovesGenerator.generate(this)
    ChessPieceType.King -> kingMovesGenerator.generate(this)
  }

  private fun createMoveGeneratorContext(
    board: ChessBoard,
    metadata: GameMetadata,
    fromField: ChessField.WithPiece,
  ) = MoveGeneratorContext(
    board = board,
    fromField = fromField,
    fromPosition = fromField.position,
    activeColor = fromField.piece.color,
    enPassantStatus = metadata.enPassantStatus,
    kingsPosition = metadata.kingsPosition,
    castlingStatus = metadata.castlingStatus,
  )
}

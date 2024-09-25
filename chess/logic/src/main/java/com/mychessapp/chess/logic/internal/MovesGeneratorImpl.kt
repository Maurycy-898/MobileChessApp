package com.mychessapp.chess.logic.internal

import com.mychessapp.chess.logic.MovesGenerator
import com.mychessapp.chess.logic.internal.model.ChessMove
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
  ): List<ChessMove> =
    board.fieldsList().flatMap {
      generateAllLegalMovesFromField(board, metadata, it)
    }

  override fun generateAllLegalMovesFromField(
    board: ChessBoard,
    metadata: GameMetadata,
    field: ChessField,
  ): List<ChessMove> =
    when (field) {
      is ChessField.Empty -> emptyList()
      is ChessField.WithPiece -> generatePieceMoves(board, metadata, field)
    }

  private fun generatePieceMoves(
    board: ChessBoard,
    metadata: GameMetadata,
    field: ChessField.WithPiece,
  ): List<ChessMove> {
    check(board.run { field.isOnChessBoard() }) {
      "Error, field is not on chessboard! : ${field.position}"
    }
    return createMoveGeneratorContext(
      board = board,
      metadata = metadata,
      fromField = field
    ).run(::generatePieceMoves)
  }

  private fun generatePieceMoves(context: MoveGeneratorContext) =
    when (context.fromField.piece.type) {
      ChessPieceType.Pawn -> pawnMovesGenerator.generateMoves(context)
      ChessPieceType.Knight -> knightMovesGenerator.generateMoves(context)
      ChessPieceType.Bishop -> bishopMovesGenerator.generateMoves(context)
      ChessPieceType.Rook -> rookMovesGenerator.generateMoves(context)
      ChessPieceType.Queen -> queenMovesGenerator.generateMoves(context)
      ChessPieceType.King -> kingMovesGenerator.generateMoves(context)
    }

  private fun createMoveGeneratorContext(
    board: ChessBoard,
    metadata: GameMetadata,
    fromField: ChessField.WithPiece,
  ) =
    MoveGeneratorContext(
      board = board,
      fromField = fromField,
      fromPosition = fromField.position,
      activeColor = fromField.piece.color,
      enPassantStatus = metadata.enPassantStatus,
      kingsStatus = metadata.kingsStatus,
      attackedFields = setOf()
    )
}

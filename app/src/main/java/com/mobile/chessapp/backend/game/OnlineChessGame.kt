package com.mobile.chessapp.backend.game

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.mobile.chessapp.backend.database.DatabaseHandler
import com.mobile.chessapp.backend.database.DatabaseMove
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.backend.game.moveUtils.CastlingMove
import com.mobile.chessapp.backend.game.moveUtils.ChessMove
import com.mobile.chessapp.backend.game.moveUtils.EnPassantMove
import com.mobile.chessapp.backend.game.moveUtils.PromotionMove

class OnlineChessGame(
    board: ChessBoard,
    playerColor: PieceColor = PieceColor.WHITE,
    oppColor: PieceColor = PieceColor.BLACK,
    private var color: PieceColor,
    @Transient var onGameOver: () -> Unit,
    @Transient var refreshBoard: () -> Unit
) : ChessGame(board, playerColor, oppColor) {

    private var newGameRefKey: String? = null

    init {
        val gamesRef = DatabaseHandler.database.getReference("games")
        var newGameRef: DatabaseReference
        if (color == PieceColor.WHITE) {
            newGameRef = gamesRef.push()
            DatabaseHandler.database.getReference("newGameRefKey").setValue(newGameRef.key)
            newGameRefKey = newGameRef.key
            addListeners()
        } else {
            DatabaseHandler.database.getReference("newGameRefKey").addValueEventListener(object : ValueEventListener {
                private var firstChange = false
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!firstChange) {
                        firstChange = true
                        return
                    }
                    newGameRef = DatabaseHandler.database.getReference("games/${snapshot.getValue<String>()}")
                    newGameRefKey = newGameRef.key
                    DatabaseHandler.database.getReference("newGameRefKey").removeEventListener(this)
                    addListeners()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Database:newGameRefKey", "onCancelled", error.toException())
                }
            })
            boardUI.flip()
        }
    }

    private fun addListeners() {
        val newGameRef = DatabaseHandler.database.getReference("games").child(newGameRefKey!!)
        val movesRef = newGameRef.child("moves")
        movesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("Database:moves", "added")
                val dbMoveMade = snapshot.getValue<DatabaseMove>() ?: return
                val moveMade: ChessMove = if (dbMoveMade.newPiece != null) {
                    dbMoveMade.chessMove!!.toPromotionMove(dbMoveMade.newPiece!!)
                } else if (dbMoveMade.isEnPassantMove) {
                    dbMoveMade.chessMove!!.toEnPassantMove()
                } else if (dbMoveMade.isCastlingMove) {
                    dbMoveMade.chessMove!!.toCastlingMove()
                } else {
                    dbMoveMade.chessMove!!
                }
                if (board.fields[moveMade.beginCol][moveMade.beginRow] != null) {
                    board.doMove(moveMade)
                    moveArchive.add(moveMade)
                    boardUI.updateFields(board)
                    onFieldClick(moveMade.endCol, moveMade.endRow, onGameOver, refreshBoard)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("Database:moves", "changed")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.i("Database:moves", "removed")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("Database:moves", "moved")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Database:moves", "onCancelled", error.toException())
            }
        })

        newGameRef.child("winner")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val winnerDb = snapshot.getValue<String>()
                    if (winnerDb == color.name) {
                        board.isGameOver = true
                        winner = color
                        onGameOver()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Database:winner", "onCancelled", error.toException())
                }
            })
    }

    override fun onFieldClick(col: Int, row: Int, onGameOver: () -> Unit, refreshBoard: () -> Unit) {
        clickCount += 1
        if (board.activeColor == color) {
            click(col, row, refreshBoard)
            if (this.board.isGameOver) {
                onGameOver()
            }
        } else {
            onCancel()
        }
        refreshBoard()
    }

    override fun prepareOpponentsTurn() {
        val newGameRef = DatabaseHandler.database.getReference("games").child(newGameRefKey!!)
        val movesRef = newGameRef.child("moves")
        val move = moveArchive.last
        val newMove = movesRef!!.push()
        newMove.setValue(DatabaseMove(move, if (move is PromotionMove) move.newPiece else null,
            move is EnPassantMove, move is CastlingMove))
    }

    override fun surrender() {
        val newGameRef = DatabaseHandler.database.getReference("games").child(newGameRefKey!!)
        board.isGameOver = true
        winner = if (color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        newGameRef.child("winner")
            .setValue(if (color == PieceColor.WHITE) "BLACK" else "WHITE")
    }
}

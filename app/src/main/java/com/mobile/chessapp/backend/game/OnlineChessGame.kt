package com.mobile.chessapp.backend.game

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mobile.chessapp.backend.database.DatabaseMove
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.backend.game.moveUtils.CastlingMove
import com.mobile.chessapp.backend.game.moveUtils.ChessMove
import com.mobile.chessapp.backend.game.moveUtils.EnPassantMove
import com.mobile.chessapp.backend.game.moveUtils.PromotionMove
import com.mobile.chessapp.ui.adapters.OnFieldClick

object DatabaseHandler {
    val database = Firebase.database
}

class OnlineChessGame(
    board: ChessBoard,
    playerColor: PieceColor = PieceColor.WHITE,
    oppColor: PieceColor = PieceColor.BLACK,
    private var color: PieceColor,
    private var onFieldClick: OnFieldClick
) : ChessGame(board, playerColor, oppColor) {
    private var newGameRef: DatabaseReference? = null
    private var movesRef: DatabaseReference? = null

    init {
        val gamesRef = DatabaseHandler.database.getReference("games")
        if (color == PieceColor.WHITE) {
            newGameRef = gamesRef.push()
            DatabaseHandler.database.getReference("newGameRefKey").setValue(newGameRef!!.key)
            movesRef = newGameRef!!.child("moves")
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
                    movesRef = newGameRef!!.child("moves")
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
        movesRef?.addChildEventListener(object : ChildEventListener {
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
                    onFieldClick.onFieldClick(moveMade.endCol, moveMade.endRow)
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

        newGameRef?.child("winner")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val winnerDb = snapshot.getValue<String>()
                if (winnerDb == color.name) {
                    board.isGameOver = true
                    winner = color
                    onFieldClick.onFieldClick(0,0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Database:winner", "onCancelled", error.toException())
            }
        })
    }

    override fun onFieldClick(col: Int, row: Int) {
        clickCount += 1
        if (board.activeColor == color) {
            click(col, row)
        } else {
            onCancel()
        }
    }

    override fun prepareOpponentsTurn() {
        val move = moveArchive.last
        val newMove = movesRef!!.push()
        newMove.setValue(DatabaseMove(move, if (move is PromotionMove) move.newPiece else null,
            move is EnPassantMove, move is CastlingMove))
    }

    override fun surrender() {
        board.isGameOver = true
        winner = if (color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        newGameRef?.child("winner")
            ?.setValue(if (color == PieceColor.WHITE) "BLACK" else "WHITE")
    }
}
package com.mobile.chessapp.backend.game

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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
import java.util.Objects

object DatabaseHandler {
    val database = Firebase.database
    var whitePlayer = false
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
            movesRef = newGameRef!!.child("moves")
            addMovesListener()
        } else {
            gamesRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("added", "added")
                    newGameRef = snapshot.ref
                    movesRef = newGameRef!!.child("moves")
                    gamesRef.removeEventListener(this)
                    addMovesListener()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("removed", "removed")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    Log.i("removed", "removed")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("moved", "moved")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Database:moves", "onCancelled", error.toException())
                }
            })
        }
        //movesRef.setValue(ArrayList<DatabaseMove>())
        if (color == PieceColor.BLACK) {
            boardUI.flip()
        }
        /*gamesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dbMoveMade = dataSnapshot.getValue<DatabaseMove>() ?: return
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

            override fun onCancelled(error: DatabaseError) {
                Log.w("Database:moves", "loadMove:onCancelled", error.toException())
            }
        })*/
    }

    private fun addMovesListener() {
        movesRef!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("added", "added")
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
                Log.i("changed", "changed")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.i("removed", "removed")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("moved", "moved")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Database:moves", "onCancelled", error.toException())
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
}
package com.mobile.chessapp.backend.game

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.backend.game.moveUtils.ChessMove

object DatabaseHandler {
    val database = Firebase.database.reference
    var whitePlayer = false
}

class OnlineChessGame(
    board: ChessBoard,
    playerColor: PieceColor = PieceColor.WHITE,
    oppColor: PieceColor = PieceColor.BLACK,
    private var color: PieceColor
) : ChessGame(board, playerColor, oppColor) {
    private val TAG = "Database:moves"

    init {
        DatabaseHandler.database.child("moves").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val moveMade = dataSnapshot.getValue<ChessMove>() ?: return
                if (board.fields[moveMade.beginCol][moveMade.beginRow] != null) {
                    board.doMove(moveMade)
                    moveArchive.add(moveMade)
                    boardUI.updateFields(board)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadMove:onCancelled", databaseError.toException())
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
        DatabaseHandler.database.child("moves").setValue(moveArchive.last)
    }
}
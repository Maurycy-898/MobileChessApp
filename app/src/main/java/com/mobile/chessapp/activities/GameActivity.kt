package com.mobile.chessapp.activities

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.chessapp.R
import com.mobile.chessapp.backend.game.ChessGame
import com.mobile.chessapp.backend.game.GameMode
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.moveUtils.MoveGenerator
import com.mobile.chessapp.ui.adapters.GameAdapter
import com.mobile.chessapp.ui.adapters.OnFieldClick

class GameActivity : AppCompatActivity(), OnFieldClick {
    private lateinit var chessGame: ChessGame
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        recyclerView = findViewById(R.id.board)

        // TODO: chess game based on how it was called
        chessGame = ChessGame(ChessBoard(), GameMode.ENGINE)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(
                this, 8, GridLayoutManager.HORIZONTAL, false)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 8)
        }

        boardAdapter = GameAdapter(this, chessGame.boardUI, this)
        recyclerView.adapter = boardAdapter
    }

    override fun onFieldClick(col: Int, row: Int) {
        if (chessGame.board.isGameOver) {
            onGameOver()
        } else {
            chessGame.onFieldClick(col, row)
            boardAdapter.notifyDataSetChanged()
        }
    }

    private fun onGameOver() {
        if (chessGame.board.blackKingAttacked) {
            Toast.makeText(this, "GAME OVER, WHITE WON!!!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "GAME OVER, BLACK WON!!!", Toast.LENGTH_SHORT).show()
        }
    }
}


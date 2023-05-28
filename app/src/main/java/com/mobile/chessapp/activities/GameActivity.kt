package com.mobile.chessapp.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.chessapp.R
import com.mobile.chessapp.backend.game.ChessGame
import com.mobile.chessapp.backend.game.EngineChessGame
import com.mobile.chessapp.backend.game.OfflineChessGame
import com.mobile.chessapp.backend.game.OnlineChessGame
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
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
        chessGame = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> savedInstanceState?.getSerializable(
                "chessGame",
                ChessGame::class.java
            )
            else -> @Suppress("DEPRECATION") savedInstanceState?.getSerializable("chessGame") as? ChessGame
        }
            ?: when (intent.getIntExtra("mode", 0)) {
                0 -> OfflineChessGame(ChessBoard())
                1 -> OnlineChessGame(
                    ChessBoard(),
                    color = PieceColor.valueOf(intent.getStringExtra("color")!!),
                    onFieldClick = this
                )
                else -> EngineChessGame(ChessBoard())
            }



        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(
                this, 8, GridLayoutManager.HORIZONTAL, false
            )
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 8)
        }

        boardAdapter = GameAdapter(this, chessGame.boardUI, this)
        recyclerView.adapter = boardAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("chessGame", chessGame)
    }

    fun surrender(view: View) {
        //TODO: popup window
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Czy na pewno chcesz poddać rozgrywkę?")
        builder.setPositiveButton("Tak") { _, _ ->

        }
        builder.setNegativeButton("Nie") { _, _ ->

        }
    }
}


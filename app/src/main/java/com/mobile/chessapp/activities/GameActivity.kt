package com.mobile.chessapp.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.chessapp.R
import com.mobile.chessapp.backend.game.*
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.ui.adapters.GameAdapter
import com.mobile.chessapp.ui.adapters.OnFieldClick

class GameActivity : AppCompatActivity(), OnFieldClick {
    private lateinit var chessGame: ChessGame
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardAdapter: GameAdapter

    @androidx.annotation.OptIn(BuildCompat.PrereleaseSdkCheck::class)
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
        onBackPressedDispatcher.addCallback(this) {
            exit()
        }
    }

    override fun onDestroy() {
        if (!chessGame.board.isGameOver) {
            if (chessGame is OnlineChessGame) {
                (chessGame as OnlineChessGame).surrender()
            }
        }
        super.onDestroy()
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
        if (chessGame.winner == PieceColor.WHITE || chessGame.board.blackKingAttacked) {
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
        if (chessGame.board.isGameOver) {
            return
        }
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Czy na pewno chcesz poddać rozgrywkę?")
        builder.setPositiveButton("Tak") { _, _ ->
            if (chessGame is OnlineChessGame) {
                (chessGame as OnlineChessGame).surrender()
            }
        }
        builder.setNegativeButton("Nie") { _, _ ->}
        val dialog = builder.create()
        dialog.show()
    }

    private fun exit() {
        if (chessGame.board.isGameOver) {
            finish()
            return
        }
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Czy na pewno chcesz opuścić rozgrywkę? (spowoduje to zwycięstwo przeciwnika)")
        builder.setPositiveButton("Tak") { _, _ ->
            finish()
        }
        builder.setNegativeButton("Nie") { _, _ ->}
        val dialog = builder.create()
        dialog.show()
    }
}


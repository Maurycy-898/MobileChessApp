package com.mobile.chessapp.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
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

    @SuppressLint("NotifyDataSetChanged")
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
                    onGameOver = { onGameOver() },
                    refreshBoard = { boardAdapter.notifyDataSetChanged() }
                )
                else -> EngineChessGame(ChessBoard())
            }
        if (chessGame is OnlineChessGame){
            (chessGame as OnlineChessGame).onGameOver = { onGameOver() }
            (chessGame as OnlineChessGame).refreshBoard = { boardAdapter.notifyDataSetChanged() }
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
            chessGame.surrender()
        }
        super.onDestroy()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onFieldClick(col: Int, row: Int) {
        chessGame.onFieldClick(col, row,
            onGameOver = { onGameOver() },
            refreshBoard = { boardAdapter.notifyDataSetChanged() }
        )
    }

    private fun onGameOver() {
        if (chessGame.winner == PieceColor.WHITE || chessGame.board.blackKingAttacked) {
            showGameOverDialog("GAME OVER, WHITE WON!!!")
        } else {
            showGameOverDialog("GAME OVER, BLACK WON!!!")
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
        builder.setMessage("Do you want to surrender?")
        builder.setPositiveButton("YES") { _, _ ->
            chessGame.surrender()
            showGameOverDialog("YOU LOST!")
        }
        builder.setNegativeButton("NO") { _, _ -> }
        builder.setCancelable(false)
        builder.create().show()
    }

    private fun exit() {
        if (chessGame.board.isGameOver) {
            finish()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to quit? (you will lose the game)")
            builder.setPositiveButton("YES") { _, _ ->
                finish()
            }
            builder.setNegativeButton("NO") { _, _ -> }
            builder.setCancelable(false)
            builder.create().show()
        }
    }

    private fun showGameOverDialog(message: String) {
        val infoDialogBuilder = AlertDialog.Builder(this)
        infoDialogBuilder.setMessage(message)
        infoDialogBuilder.setPositiveButton("OK") { _, _ ->
            finish()
        }
        infoDialogBuilder.setCancelable(false)
        infoDialogBuilder.create().show()
    }
}


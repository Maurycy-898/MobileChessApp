package com.mobile.chessapp.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.chessapp.R
import com.mobile.chessapp.backend.game.ChessBoard

class GameActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardAdapter: BoardAdapter
    private var chessBoard = ChessBoard()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        recyclerView = findViewById(R.id.board)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(this, 8, GridLayoutManager.HORIZONTAL, false)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 8)
        }
        boardAdapter = BoardAdapter(chessBoard, this)
        recyclerView.adapter = boardAdapter
    }
}

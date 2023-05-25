package com.mobile.chessapp.activities

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard

class BoardAdapter(
    private var chessBoard: ChessBoard,
    private val mContext: Context
    ) : RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    private val pieces = listOf(listOf("", "♟", "♞", "♝", "♜", "♛", "♚"), listOf("", "♙", "♘", "♗", "♖", "♕", "♔"))

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: TextView = FieldView(mContext)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(view, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        view.gravity = Gravity.CENTER
        return ViewHolder(view)
    }

    override fun getItemCount(): Int { return 64 }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var xPos = position % 8
        var yPos = position / 8
        if (mContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            xPos = yPos.also {yPos = xPos}
        }
        if ((xPos + yPos) % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EEEED2"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#964B00"))
        }
        val chessPiece = chessBoard.fields[xPos][yPos]
        if (chessPiece != null)
            (holder.itemView as TextView).text = pieces[chessPiece.color.ordinal][chessPiece.type.ordinal]
    }
}
package com.mobile.chessapp.ui.adapters

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobile.chessapp.backend.game.BoardUI
import com.mobile.chessapp.backend.game.FieldUI
import com.mobile.chessapp.backend.game.boardUtils.BOARD_SIZE
import com.mobile.chessapp.ui.colors.PROMPTED_FIELD_COLOR
import com.mobile.chessapp.ui.views.FieldView

class GameAdapter(
    private val mContext: Context,
    private var boardUI:  BoardUI,
    private val onClick:  OnFieldClick
    ) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: TextView = FieldView(mContext)
        itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(itemView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        itemView.gravity = Gravity.CENTER
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return BOARD_SIZE * BOARD_SIZE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var xPos = position % BOARD_SIZE; var yPos = position / BOARD_SIZE
        if (mContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            xPos = yPos.also { yPos = xPos }
        }
        holder.bind(boardUI.getField(xPos, yPos))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fieldUI: FieldUI) {
            (itemView as TextView).text = fieldUI.symbol

            if (!fieldUI.prompted){
                itemView.setBackgroundResource(fieldUI.color)
            } else {
                itemView.setBackgroundResource(PROMPTED_FIELD_COLOR)
            }
            itemView.setOnClickListener {
                onClick.onFieldClick(fieldUI.col, fieldUI.row)
            }
        }
    }
}

interface OnFieldClick {
    fun onFieldClick(col: Int, row: Int)
}
package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem

class ChatItemTouchHelperCallback(
    val adapter: ChatAdapter,
    val swipeListener: (ChatItem) -> Unit
): ItemTouchHelper.Callback() {
    private val bgRect = RectF()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val iconBounds = Rect()

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if(viewHolder is ItemTouchViewHolder) {
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START)
        }
        else {
            makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeListener.invoke(adapter.items[viewHolder.adapterPosition])
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is ItemTouchViewHolder) viewHolder.onItemCleared()
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val viewItem = viewHolder.itemView
            drawBackground(canvas, viewItem, dX)
            drawIcon(canvas, viewItem, dX)
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawIcon(canvas: Canvas, viewItem: View, dX: Float) {
        val icon = viewItem.resources.getDrawable(R.drawable.ic_archive_black_24dp, viewItem.context.theme)
        val iconSize = viewItem.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = viewItem.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
        val margin = (viewItem.bottom - viewItem.top - iconSize) / 2

        with(iconBounds) {
            left = viewItem.right + dX.toInt() + space
            top = viewItem.top + margin
            right = viewItem.right + dX.toInt() + iconSize + space
            bottom = viewItem.bottom - margin
        }

        icon.bounds = iconBounds
        icon.draw(canvas)
    }

    private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float) {
        with(bgRect) {
            left = viewItem.right.toFloat() + dX
            top = viewItem.top.toFloat()
            right = viewItem.right.toFloat()
            bottom = viewItem.bottom.toFloat()
        }
        with(bgPaint) {
            color = viewItem.resources.getColor(R.color.color_primary_dark, viewItem.context.theme)
        }
        canvas.drawRect(bgRect, bgPaint)
    }
}

interface ItemTouchViewHolder {
    fun onItemSelected()
    fun onItemCleared()
}
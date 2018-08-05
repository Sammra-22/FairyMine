package com.nordic.fairymine.common.view.widgets

import android.content.Context
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.nordic.fairymine.R

/**
 * Created by Sam22 on 3/16/18.
 */
class VerticalListDivider(val context: Context) : DividerItemDecoration(context, DividerItemDecoration.VERTICAL) {

    init {
        ContextCompat.getDrawable(context, R.drawable.bg_list_divider)?.let {
            setDrawable(it)
        }
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view)
        // hide the divider for the last child
        if (position == parent.adapter.itemCount - 1) {
            outRect?.setEmpty()
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }
}
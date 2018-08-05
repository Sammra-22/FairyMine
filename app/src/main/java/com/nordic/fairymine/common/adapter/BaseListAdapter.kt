package com.nordic.fairymine.common.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nordic.fairymine.common.model.ViewState
import com.nordic.fairymine.common.view.DataBindViewHolder
import com.nordic.fairymine.databinding.RowEmptyBinding
import com.nordic.fairymine.databinding.RowErrorBinding
import com.nordic.fairymine.databinding.RowProgressBinding

/**
 * Created by Sam22 on 3/14/18.
 */
abstract class BaseListAdapter<T> : RecyclerView.Adapter<DataBindViewHolder<*>>() {

    private var items = listOf<T>()
    private var state = ViewState.PROGRESS

    fun setItems(items: List<T>) {
        this.items = items
        updateState(ViewState.SUCCESS)
    }

    fun setProgress() = updateState(ViewState.PROGRESS)

    fun setError() = if (items.isEmpty()) {
        updateState(ViewState.FAILURE)
    } else {
        updateState(ViewState.SUCCESS)
    }

    fun getItems() = items

    fun getItemAt(position: Int): T = items[position]

    private fun updateState(newState: ViewState) {
        state = newState
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (items.isEmpty()) 1 else items.size

    override fun getItemViewType(position: Int): Int = when {
        state == ViewState.PROGRESS -> ITEM_VIEW_TYPE_PROGRESS
        state == ViewState.FAILURE -> ITEM_VIEW_TYPE_ERROR
        state == ViewState.SUCCESS && items.isEmpty() -> ITEM_VIEW_TYPE_EMPTY
        else -> ITEM_VIEW_TYPE_CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindViewHolder<*> = when (viewType) {
        ITEM_VIEW_TYPE_EMPTY -> DataBindViewHolder(RowEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        ITEM_VIEW_TYPE_PROGRESS -> DataBindViewHolder(RowProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        ITEM_VIEW_TYPE_ERROR -> DataBindViewHolder(RowErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else -> getContentViewHolder(parent.context, parent)
    }

    abstract fun getContentViewHolder(context: Context?, root: ViewGroup?): DataBindViewHolder<*>

    companion object {
        @JvmStatic protected val ITEM_VIEW_TYPE_EMPTY = 100
        @JvmStatic protected val ITEM_VIEW_TYPE_PROGRESS = 200
        @JvmStatic protected val ITEM_VIEW_TYPE_ERROR = 300
        @JvmStatic protected val ITEM_VIEW_TYPE_CONTENT = 400
    }

}
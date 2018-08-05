package com.nordic.fairymine.account.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nordic.fairymine.R
import com.nordic.fairymine.account.AccountEntityVM
import com.nordic.fairymine.account.listener.PendingOrderListener
import com.nordic.fairymine.api.model.Offer
import com.nordic.fairymine.common.adapter.BaseListAdapter
import com.nordic.fairymine.common.view.DataBindViewHolder
import com.nordic.fairymine.databinding.RowAccountHeaderBinding
import com.nordic.fairymine.databinding.RowAccountOrderInterruptedBinding
import com.nordic.fairymine.databinding.RowEmptyBinding
import com.nordic.fairymine.databinding.RowErrorBinding

/**
 * Created by Sam22 on 3/15/18.
 */
class InterruptedOrdersAdapter(private val listener: PendingOrderListener) : BaseListAdapter<Offer>() {

    override fun getItemCount(): Int = super.getItemCount() + 1

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> ITEM_VIEW_TYPE_HEADER
        else -> super.getItemViewType(position)
    }

    override fun getContentViewHolder(context: Context?, root: ViewGroup?): DataBindViewHolder<*> =
            DataBindViewHolder(RowAccountOrderInterruptedBinding
                    .inflate(LayoutInflater.from(context), root, false))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindViewHolder<*> = when (viewType) {
        ITEM_VIEW_TYPE_HEADER -> DataBindViewHolder(RowAccountHeaderBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: DataBindViewHolder<*>, position: Int) {
        val context = holder.binding.root.context
        when (holder.binding) {
            is RowAccountHeaderBinding -> holder.binding.label = context.getString(R.string.account_orders_interrupted_header_title)
            is RowEmptyBinding -> holder.binding.label = context.getString(R.string.account_orders_pending_empty)
            is RowErrorBinding -> holder.binding.label = context.getString(R.string.error_network)
            is RowAccountOrderInterruptedBinding -> {
                val offer = getItemAt(position - 1)
                holder.binding.viewModel = AccountEntityVM(offer, { listener.onResumeInterruptedOrder(it) })
            }
        }
    }

    companion object {
        const val ITEM_VIEW_TYPE_HEADER = 1
    }
}
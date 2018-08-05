package com.nordic.fairymine.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nordic.fairymine.R
import com.nordic.fairymine.api.model.Offer
import com.nordic.fairymine.common.adapter.BaseListAdapter
import com.nordic.fairymine.common.view.DataBindViewHolder
import com.nordic.fairymine.databinding.RowEmptyBinding
import com.nordic.fairymine.databinding.RowErrorBinding
import com.nordic.fairymine.databinding.RowOfferingProductBinding
import com.nordic.fairymine.product.listener.AdOfferActionListener

/**
 * Created by Sam22 on 1/7/18.
 */
class AdOfferListAdapter(val listener: AdOfferActionListener) : BaseListAdapter<Offer>() {

    override fun getContentViewHolder(context: Context?, root: ViewGroup?): DataBindViewHolder<*> =
            DataBindViewHolder(RowOfferingProductBinding.inflate(LayoutInflater.from(context), root, false))


    override fun onBindViewHolder(holder: DataBindViewHolder<*>, position: Int) {
        val context = holder.binding.root.context
        when (holder.binding) {
            is RowEmptyBinding -> holder.binding.label = context.getString(R.string.offers_ads_empty)
            is RowErrorBinding -> {
                holder.binding.label = context.getString(R.string.error_network)
                holder.binding.retryBtn.setOnClickListener { listener.onReloadAdOffers() }
            }
            is RowOfferingProductBinding -> {
                holder.binding.product = getItemAt(position)
                holder.itemView.setOnClickListener { listener.onAdOfferDetail(getItemAt(position)) }
                holder.binding.btnActivate.setOnClickListener { listener.onAdOfferSelected(getItemAt(position)) }
            }
        }
    }

}
package com.nordic.fairymine.common.view

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by Sam22 on 12/21/17.
 */
class DataBindViewHolder<out V: ViewDataBinding>(val binding: V): RecyclerView.ViewHolder(binding.root)
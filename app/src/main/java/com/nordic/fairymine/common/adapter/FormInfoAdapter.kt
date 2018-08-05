package com.nordic.fairymine.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.nordic.fairymine.R
import com.nordic.fairymine.common.form.*
import com.nordic.fairymine.common.view.DataBindViewHolder
import com.nordic.fairymine.common.view.widgets.MultiPickSpinner
import com.nordic.fairymine.databinding.RowRegistrationCustomerInfoMultiSpinnerBinding
import com.nordic.fairymine.databinding.RowRegistrationCustomerInfoSpinnerBinding
import com.nordic.fairymine.databinding.RowRegistrationCustomerInfoTextBinding

/**
 * Created by Sam22 on 12/21/17.
 */
class FormInfoAdapter(private var form: Form = Form()) : RecyclerView.Adapter<DataBindViewHolder<*>>() {

    fun populate(form: Form) {
        this.form = form
        notifyDataSetChanged()
    }

    fun getForm(): Form = form

    override fun getItemViewType(position: Int): Int {
        val userInfoType = form.infoList[position].infoType
        return when (userInfoType) {
            is InfoRowType.TextInput -> VIEW_TYPE_TEXT_INPUT
            is InfoRowType.SingleSelect -> VIEW_TYPE_SINGLE_CHOICE
            is InfoRowType.MultipleSelect -> VIEW_TYPE_MULTIPLE_CHOICE
        }
    }

    override fun getItemCount(): Int = form.infoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SINGLE_CHOICE ->
                DataBindViewHolder(RowRegistrationCustomerInfoSpinnerBinding.inflate(inflater, parent, false))
            VIEW_TYPE_MULTIPLE_CHOICE ->
                DataBindViewHolder(RowRegistrationCustomerInfoMultiSpinnerBinding.inflate(inflater, parent, false))
            else ->
                DataBindViewHolder(RowRegistrationCustomerInfoTextBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: DataBindViewHolder<*>, position: Int) {
        val customerInfo = form.infoList[position]
        (holder.binding as? RowRegistrationCustomerInfoTextBinding)?.customerInfo = customerInfo
        (holder.binding as? RowRegistrationCustomerInfoSpinnerBinding)?.let {
            it.customerInfo = customerInfo
            val context = it.root.context
            val selectInfoType = customerInfo.infoType as InfoRowType.SingleSelect
            val alternatives = mutableMapOf("" to "").apply {
                putAll(selectInfoType.alternatives)
            }
            val adapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, alternatives.keys.toList())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.spinner.apply {
                this.adapter = adapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) = Unit

                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) =
                            customerInfo.value.set(alternatives[parent.getItemAtPosition(position) as String])
                }
            }
        }
        (holder.binding as? RowRegistrationCustomerInfoMultiSpinnerBinding)?.let {
            it.customerInfo = customerInfo
            val selectInfoType = customerInfo.infoType as InfoRowType.MultipleSelect
            val alternatives = selectInfoType.alternatives
            val triggerSpinner = it.spinner::performClick
            it.spinnerButton.setOnClickListener { triggerSpinner() }
            it.spinner.setItems(alternatives.keys.toList(), object : MultiPickSpinner.Listener {
                override fun onItemsSelected(selected: Set<String>) {
                    customerInfo.value.set(selected.map { alternatives[it] }.joinToString())
                }
            })
        }
    }

    companion object {
        const val VIEW_TYPE_TEXT_INPUT = 1
        const val VIEW_TYPE_SINGLE_CHOICE = 2
        const val VIEW_TYPE_MULTIPLE_CHOICE = 3
    }

}
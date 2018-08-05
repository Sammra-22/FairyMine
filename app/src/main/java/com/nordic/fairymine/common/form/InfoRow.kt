package com.nordic.fairymine.common.form

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.adapters.TextViewBindingAdapter

/**
 * Created by Sam22 on 12/21/17.
 */
data class InfoRow(val key: String, val label: String, val infoType: InfoRowType) {

    val value = ObservableField<String>()
    val hasError = ObservableBoolean(false)

    init {
        if (infoType is InfoRowType.TextInput) {
            value.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(p0: Observable?, p1: Int) =
                        hasError.set(value.get().isNullOrBlank())
            })
        }
    }

    val inputType: Int
        get() = infoType.textInputType


    val textWatcher = TextViewBindingAdapter.AfterTextChanged {
        if (value.get() != it.toString()) {
            value.set(it.toString())
        }
    }

}
package com.nordic.fairymine.common.view.widgets

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.nordic.fairymine.common.adapter.FormInfoAdapter
import com.nordic.fairymine.common.form.Form
import com.nordic.fairymine.databinding.LayoutFormBinding

/**
 * Created by Sam22 on 3/8/18.
 */
class FormLayout @JvmOverloads constructor(context: Context,
                                           attrs: AttributeSet? = null,
                                           @AttrRes defStyleAttrs: Int = 0) : FrameLayout(context, attrs, defStyleAttrs) {

    private val binding: LayoutFormBinding = LayoutFormBinding.inflate(LayoutInflater.from(context), this, true)
    private var adapter = FormInfoAdapter()

    init {
        binding.formInputList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@FormLayout.adapter
        }
    }

    fun setFormData(form: Form) {
        adapter.populate(form)
    }

    fun setFormButtonText(@StringRes textResId: Int) {
        binding.formDoneButton.text = context.getText(textResId)
    }

    fun setOnFormValidated(action: () -> Unit = {}) {
        binding.formDoneButton.setOnClickListener {
            if (adapter.getForm().isValid) {
                action.invoke()
            }
        }
    }


}
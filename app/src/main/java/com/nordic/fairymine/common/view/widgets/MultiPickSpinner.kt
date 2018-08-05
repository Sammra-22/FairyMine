package com.nordic.fairymine.common.view.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.Spinner


/**
 * Created by Sam22 on 4/5/18.
 */
class MultiPickSpinner @JvmOverloads constructor(context: Context,
                                                 attributeSet: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) : Spinner(context, attributeSet, defStyleAttr), DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private var items: LinkedHashMap<String, Boolean> = linkedMapOf()
    private var listener: Listener? = null

    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        val item = items.keys.toList()[which]
        if (isChecked) {
            items.put(item, true)
        } else {
            items.put(item, false)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        val selected = items.filter { it.value }.keys
        val spinnerText = selected.joinToString(limit = 4)
        val adapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,
                arrayOf(spinnerText))
        setAdapter(adapter)
        listener?.onItemsSelected(selected)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean {
        AlertDialog.Builder(context)
                .setMultiChoiceItems(items.keys.toTypedArray(), items.values.toBooleanArray(), this)
                .setPositiveButton(android.R.string.ok, { dialog, _ -> dialog.cancel() })
                .setOnCancelListener(this)
                .show()
        return true
    }

    fun setItems(items: List<String>, listener: Listener? = null, initialText: String = "") {
        this.items = LinkedHashMap(items.associateBy({ it }, { false }))
        this.listener = listener

        // Initial text on the spinner
        val adapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,
                arrayOf(initialText))
        setAdapter(adapter)
    }


    interface Listener {
        fun onItemsSelected(selected: Set<String>)
    }

}
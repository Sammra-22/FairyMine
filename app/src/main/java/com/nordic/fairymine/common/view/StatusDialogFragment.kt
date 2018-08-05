package com.nordic.fairymine.common.view

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.nordic.fairymine.R
import com.nordic.fairymine.databinding.FragmentDialogStatusBinding

/**
 * Created by Sam22 on 3/4/18.
 */
class StatusDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogStatusBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = FragmentDialogStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            if (window != null) {
                window.attributes.windowAnimations = R.style.AppWidget_DialogTransition
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleText = arguments.getString(EXTRA_TITLE)
        val bodyText = arguments.getString(EXTRA_BODY)
        val isSuccess = arguments.getBoolean(EXTRA_STATUS_SUCCESS)
        binding.apply {
            dialogTitle.text = titleText
            dialogBody.text = bodyText
            if (isSuccess) {
                dialogIcon.setImageResource(R.drawable.ic_status_success)
            } else {
                dialogIcon.setImageResource(R.drawable.ic_status_error)
            }
            okBtn.setOnClickListener { close() }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun close() {
        Handler().postDelayed({ dismiss() }, 300)
    }

    companion object {
        private val TAG = StatusDialogFragment::class.java.name!!
        private val EXTRA_TITLE = "EXTRA_TITLE"
        private val EXTRA_BODY = "EXTRA_BODY"
        private val EXTRA_STATUS_SUCCESS = "EXTRA_STATUS_SUCCESS"

        fun show(fm: FragmentManager, title: String, body: String, isSuccess: Boolean) {
            val dialogFragment = fm.findFragmentByTag(TAG)
            if (dialogFragment == null) {
                val dialog = StatusDialogFragment()
                dialog.arguments = Bundle().apply {
                    putString(EXTRA_TITLE, title)
                    putString(EXTRA_BODY, body)
                    putBoolean(EXTRA_STATUS_SUCCESS, isSuccess)
                }
                dialog.show(fm, TAG)
            }
        }
    }
}
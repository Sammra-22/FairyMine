package com.nordic.fairymine.common.view

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.nordic.fairymine.R
import com.nordic.fairymine.common.utils.parseHtml
import com.nordic.fairymine.databinding.FragmentDialogActionBinding

/**
 * Created by Sam22 on 2/24/18.
 */
class ActionDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogActionBinding
    private var onDialogActionConfirm: () -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = FragmentDialogActionBinding.inflate(inflater, container, false)
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
        val titleText = arguments?.getString(EXTRA_TITLE)
        val actionBtnText = arguments?.getString(EXTRA_ACTION_BTN_TEXT)
        val dismissBtnText = arguments?.getString(EXTRA_DISMISS_BTN_TEXT)
        binding.apply {
            txtDialogTitle.text = titleText
            txtDialogTitle.visibility = if (titleText != null) View.VISIBLE else View.GONE

            txtDialogBody.text = arguments?.getString(EXTRA_BODY)?.parseHtml()
            txtDialogBody.movementMethod = LinkMovementMethod.getInstance()

            btnDialogDismiss.text = dismissBtnText
            btnDialogDismiss.visibility = if (dismissBtnText != null) View.VISIBLE else View.GONE

            btnDialogAction.text = actionBtnText
            btnDialogAction.visibility = if (actionBtnText != null) View.VISIBLE else View.GONE

            btnDialogAction.setOnClickListener {
                onDialogActionConfirm()
                dismiss()
            }
            btnDialogDismiss.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private val TAG = ActionDialogFragment::class.java.name!!
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_BODY = "EXTRA_BODY"
        private const val EXTRA_DISMISS_BTN_TEXT = "EXTRA_DISMISS_BTN_TEXT"
        private const val EXTRA_ACTION_BTN_TEXT = "EXTRA_ACTION_BTN_TEXT"

        fun show(fm: FragmentManager?, title: String, body: String, dismissBtnText: String = "Ok",
                 actionBtnText: String? = null, onDialogActionConfirm: (() -> Unit)? = null) {

            if (fm != null && fm.findFragmentByTag(TAG) == null) {
                val dialog = ActionDialogFragment()
                dialog.arguments = Bundle().apply {
                    putString(EXTRA_TITLE, title)
                    putString(EXTRA_BODY, body)
                    putString(EXTRA_DISMISS_BTN_TEXT, dismissBtnText)
                    putString(EXTRA_ACTION_BTN_TEXT, actionBtnText)
                }
                onDialogActionConfirm?.let {
                    dialog.onDialogActionConfirm = it
                }

                dialog.show(fm, TAG)
            }
        }
    }
}
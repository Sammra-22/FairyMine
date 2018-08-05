package com.nordic.fairymine.common.view

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.nordic.fairymine.R
import com.nordic.fairymine.common.model.DialogEvent
import com.nordic.fairymine.databinding.FragmentDialogProgressBinding
import io.reactivex.subjects.BehaviorSubject


/**
 * Created by Sam22 on 3/14/18.
 */
class ProgressDialogFragment : DialogFragment() {

    lateinit var binding: FragmentDialogProgressBinding

    val subject: BehaviorSubject<DialogEvent> = BehaviorSubject.create<DialogEvent>()

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = FragmentDialogProgressBinding.inflate(inflater, container, false)
        subject.onNext(DialogEvent.SHOW)
        binding.btnDialogDismiss.setOnClickListener {
            dismiss()
        }
        binding.btnDialogDismiss.visibility = if (arguments.getBoolean(EXTRA_CANCELABLE, true)) View.VISIBLE else View.GONE
        binding.txtDialog.text = arguments.getString(EXTRA_MESSAGE)
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        subject.onNext(DialogEvent.CANCEL)
        subject.onComplete()
    }

    companion object {
        val TAG = ProgressDialogFragment::class.java.name!!
        private val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        private val EXTRA_CANCELABLE = "EXTRA_CANCELABLE"
        private var instance: ProgressDialogFragment? = null

        fun show(fm: FragmentManager, message: String, cancelable: Boolean = true): ProgressDialogFragment {
            val dialogFragment = if (fm.findFragmentByTag(TAG) == null) {
                ProgressDialogFragment().apply {
                    arguments = Bundle().also {
                        it.putString(EXTRA_MESSAGE, message)
                        it.putBoolean(EXTRA_CANCELABLE, cancelable)
                    }
                    show(fm, TAG)
                }
            } else {
                fm.findFragmentByTag(TAG) as ProgressDialogFragment
            }
            instance = dialogFragment
            return dialogFragment
        }

        fun dismiss() = instance?.dismissAllowingStateLoss() ?: Unit
    }
}


package com.nordic.fairymine.common.utils

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Sam22 on 2/8/18.
 */

fun View.showBriefSnackbar(@StringRes textResId: Int, vararg args: Any) = when {
    args.isEmpty() -> Snackbar.make(this, context.getString(textResId), Snackbar.LENGTH_SHORT).show()
    else -> Snackbar.make(this, context.getString(textResId, *args), Snackbar.LENGTH_SHORT).show()
}

fun View?.hideKeyboard() = this?.let {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.dpToPx(dp: Float): Int =
        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics))


fun String.formatHighlightedText(highlights: Array<String>, links: Array<String>? = null,
                                 @ColorInt hiColor: Int, onClickLink: ((String) -> Boolean)? = null): Spannable {

    val fullText = SpannableStringBuilder(this)
    highlights.forEachIndexed { index, highlight ->
        val start = this.subSequence(0, this.length).indexOf(highlight)
        if (start >= 0) {
            val stop = start + highlight.length
            if (links != null && index < links.size) {
                val link = links[index]
                fullText.setSpan(object : URLSpan(link) {
                    override fun updateDrawState(textPaint: TextPaint) {
                        super.updateDrawState(textPaint)
                        textPaint.isUnderlineText = false
                        textPaint.typeface = Typeface.DEFAULT
                    }

                    override fun onClick(widget: View?) {
                        if (onClickLink?.invoke(link) != true) {
                            super.onClick(widget)
                        }
                    }
                }, start, stop, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            fullText.setSpan(ForegroundColorSpan(hiColor), start, stop, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            fullText.setSpan(TypefaceSpan("sans-serif-medium"), start, stop, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        }
    }
    return fullText
}

fun String.parseHtml(): Spanned =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT
                    and Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(this)
        }


fun Context.color(@ColorRes colorResId: Int) = ContextCompat.getColor(this, colorResId)

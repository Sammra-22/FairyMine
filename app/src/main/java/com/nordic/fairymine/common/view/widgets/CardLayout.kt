package com.nordic.fairymine.common.view.widgets

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.nordic.fairymine.common.utils.dpToPx

/**
 * Created by Sam22 on 3/19/18.
 */
class CardLayout @JvmOverloads constructor(context: Context,
                                           attrs: AttributeSet? = null,
                                           @AttrRes defStyleAttrs: Int = 0) : FrameLayout(context, attrs, defStyleAttrs) {

    private val marginVertical = context.dpToPx(6f)
    private val marginHorizontal = context.dpToPx(12f)
    private val radius = context.dpToPx(6f).toFloat()

    override fun onFinishInflate() {
        super.onFinishInflate()
        val frame = CardView(context)
        frame.radius = radius
        frame.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical)
        }
        val child = getChildAt(0)
        val childLayoutParams = child.layoutParams
        removeView(child)
        frame.addView(child)
        child.layoutParams = childLayoutParams
        addView(frame)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val descendant = (getChildAt(0) as FrameLayout).getChildAt(0)
        val contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(descendant.measuredHeight,
                MeasureSpec.getMode(heightMeasureSpec))
        val newHeightMeasureSpec = if (contentHeightMeasureSpec > heightMeasureSpec) {
            contentHeightMeasureSpec
        } else {
            heightMeasureSpec
        }
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

}
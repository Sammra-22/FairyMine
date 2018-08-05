package com.nordic.fairymine.common.form

import android.text.InputType

/**
 * Created by Sam22 on 2/25/18.
 */
sealed class InfoRowType{

    abstract val textInputType: Int

    data class TextInput(val inputType: Int = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) : InfoRowType() {
        override val textInputType = inputType
    }

    data class SingleSelect(val alternatives: Map<String, String> = mapOf()) : InfoRowType() {
        override val textInputType: Int = 0
    }

    data class MultipleSelect(val alternatives: Map<String, String> = mapOf()) : InfoRowType() {
        override val textInputType: Int = 0
    }

}


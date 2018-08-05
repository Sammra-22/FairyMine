package com.nordic.fairymine.account

import com.nordic.fairymine.common.model.ValueUnit

/**
 * Created by Sam22 on 3/15/18.
 */
class AccountEntityVM<T : ValueUnit>(val entity: T, private val action: (T) -> Unit) {

    val value
        get() = entity.dataValue

    val unit
        get() = entity.dataUnit

    fun validateAction(entity: T) = action.invoke(entity)

}
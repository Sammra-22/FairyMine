package com.nordic.fairymine.api.model

import com.nordic.fairymine.common.model.ValueUnit

/**
 * Created by Sam22 on 2/27/18.
 */
data class Voucher(
        val pin: String,
        val valid: Boolean,
        override val dataValue: Double,
        override val dataUnit: String
) : ValueUnit()
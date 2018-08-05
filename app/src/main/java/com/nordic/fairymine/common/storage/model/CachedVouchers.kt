package com.nordic.fairymine.common.storage.model

import com.nordic.fairymine.api.model.Voucher


/**
 * Created by Sam22 on 3/21/18.
 */
data class CachedVouchers(
        val vouchers: ArrayList<Voucher> = arrayListOf()
)
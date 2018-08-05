package com.nordic.fairymine.account.listener

import com.nordic.fairymine.api.model.Voucher

/**
 * Created by Sam22 on 3/4/18.
 */
interface VoucherActionListener {

    fun onDisplayVoucherCode(voucher: Voucher)

    fun onReloadVouchers()
}
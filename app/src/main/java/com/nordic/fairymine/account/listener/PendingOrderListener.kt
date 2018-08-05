package com.nordic.fairymine.account.listener

import com.nordic.fairymine.api.model.Offer

/**
 * Created by Sam22 on 3/15/18.
 */
interface PendingOrderListener {

    fun onResendPendingTransaction(offer: Offer)

    fun onResumeInterruptedOrder(offer: Offer): Boolean
}
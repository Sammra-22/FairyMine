package com.nordic.fairymine

import com.nordic.fairymine.api.model.Offer
import com.nordic.fairymine.common.model.ProgressInstance

/**
 * Created by Sam22 on 3/19/18.
 */
interface OrderInterface {

    fun initiateAdOrder(offer: Offer): Boolean

    fun sendOrder(offerId: Long)

    fun persistPendingOrder(offerId: Long, progress: ProgressInstance? = null)

    fun discardPendingOrder(offerId: Long, progress: ProgressInstance? = null)

}
package com.nordic.fairymine.common.storage.model

import com.nordic.fairymine.api.model.Offer

/**
 * Created by Sam22 on 3/21/18.
 */
data class CachedOffers(
        val offers: ArrayList<Offer> = arrayListOf()
)
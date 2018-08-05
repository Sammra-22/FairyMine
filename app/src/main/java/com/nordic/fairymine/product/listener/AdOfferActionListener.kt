package com.nordic.fairymine.product.listener

import com.nordic.fairymine.api.model.Offer

/**
 * Created by Sam22 on 2/4/18.
 */
interface AdOfferActionListener {

    fun onAdOfferDetail(offer: Offer)

    fun onAdOfferSelected(offer: Offer): Boolean

    fun onReloadAdOffers()
}
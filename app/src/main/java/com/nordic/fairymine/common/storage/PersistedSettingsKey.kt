package com.nordic.fairymine.common.storage

import com.nordic.fairymine.common.storage.model.CachedOffers
import com.nordic.fairymine.common.storage.model.CachedVouchers
import com.nordic.fairymine.common.storage.model.PendingOrders

/**
 * Created by Sam22 on 12/30/17.
 */
enum class PersistedSettingsKey(val keyName: String, val defaultValue: Any) {

    CUSTOMER_ACCOUNT_ID("customer_account_id", -1L),
    CUSTOMER_PENDING_ORDERS("customer_pending_orders", PendingOrders()),
    CACHED_OFFERS("cached_offers", CachedOffers()),
    CACHED_VOUCHERS("cached_vouchers", CachedVouchers()),
    INVALID("invalid_key", -1);

    companion object {
        operator fun get(keyValue: String): PersistedSettingsKey =
                values().firstOrNull { it.keyName == keyValue } ?: INVALID
    }
}
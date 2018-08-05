package com.nordic.fairymine.api.service

import com.nordic.fairymine.api.factory.ApiServiceGenerator
import com.nordic.fairymine.api.model.*
import com.nordic.fairymine.common.utils.KotLog
import com.nordic.fairymine.api.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException

/**
 * Created by Sam22 on 1/6/18.
 */
class CustomerApiService(factory: ApiServiceGenerator) {

    private val customerApi: CustomerApi = factory.createApiService(CustomerApi::class.java)

    fun authenticateCustomer(user: User): Flowable<ResponseArray<Customer>> =
            customerApi.authenticate(user).retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Authentication error") }

    fun getAllOffers(): Flowable<ResponseArray<Offer>> =
            customerApi.fetchOffers().retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Get offers error") }

    fun getVouchers(customer: Customer): Flowable<ResponseArray<Voucher>> =
            customerApi.fetchVouchers(customer.id).retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Get vouchers error") }

    fun getAdSegment(customer: Customer, offer: Offer): Flowable<AdSegment> =
            customerApi.fetchAdSegment(customer.id, offer.id).retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Get AdSegment not found") }

    fun sendOrder(customer: Customer, offerId: Long): Flowable<Voucher> =
            customerApi.sendOrder(customer, offerId).retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Order error for offer id $offerId") }

    companion object {
        private val TAG = CustomerApiService::class.java.name
        const val RETRY_COUNT: Long = 2
    }
}
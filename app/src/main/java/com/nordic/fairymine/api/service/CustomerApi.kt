package com.nordic.fairymine.api.service

import com.nordic.fairymine.api.model.*
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Sam22 on 1/6/18.
 */
interface CustomerApi {

    @POST("api/authentication/mail")
    fun authenticate(@Body user: User): Flowable<ResponseArray<Customer>>

    @POST("api/transaction/order/{offerId}")
    fun sendOrder(@Body customer: Customer, @Path("offerId") offerId: Long): Flowable<Voucher>

    @GET("api/offers")
    fun fetchOffers(): Flowable<ResponseArray<Offer>>

    @GET("api/customers/{customerId}/vouchers")
    fun fetchVouchers(@Path("customerId") customerId: Long): Flowable<ResponseArray<Voucher>>

    @GET("api/customers/{customerId}/adSegment/{offerId}")
    fun fetchAdSegment(@Path("customerId") customerId: Long, @Path("offerId") offerId: Long): Flowable<AdSegment>
}
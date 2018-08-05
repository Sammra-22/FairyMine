package com.nordic.fairymine.api.service

import com.nordic.fairymine.api.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Sam22 on 1/6/18.
 */
interface RegistrationApi {

    @GET("api/registration/countries")
    fun fetchCountries(): Flowable<List<String>>

    @GET("api/registration/form/{country}")
    fun fetchUserForm(@Path("country") country: String): Flowable<User>

    @POST("api/customers/")
    fun registerCustomer(@Body user: User): Completable

}
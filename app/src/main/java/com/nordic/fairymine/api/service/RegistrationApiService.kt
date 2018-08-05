package com.nordic.fairymine.api.service

import com.nordic.fairymine.api.factory.ApiServiceGenerator
import com.nordic.fairymine.common.utils.KotLog
import com.nordic.fairymine.api.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException

/**
 * Created by Sam22 on 1/6/18.
 */
class RegistrationApiService(factory: ApiServiceGenerator) {

    private val registerApi: RegistrationApi = factory.createApiService(RegistrationApi::class.java)


    fun getCountryList(): Flowable<List<String>> =
            registerApi.fetchCountries().retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Get: Country list error") }

    fun getUserForm(country: String): Flowable<User> =
            registerApi.fetchUserForm(country).retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Get: User form error") }

    fun registerCustomer(user: User): Completable =
            registerApi.registerCustomer(user).retry(RETRY_COUNT, { it is SocketTimeoutException }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread()).doOnError { KotLog.e(TAG, "Post: Customer creation error") }


    companion object {
        private val TAG = RegistrationApiService::class.java.name
        const val RETRY_COUNT: Long = 2
    }
}
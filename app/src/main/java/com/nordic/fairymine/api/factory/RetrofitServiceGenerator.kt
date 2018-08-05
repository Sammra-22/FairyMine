package com.nordic.fairymine.api.factory

import retrofit2.Retrofit

/**
 * Created by Sam22 on 1/6/18.
 */
class RetrofitServiceGenerator(private val retrofit: Retrofit) : ApiServiceGenerator {

    override fun <S> createApiService(serviceClass: Class<S>): S = retrofit.create(serviceClass)

    override val currentHost: String
        get() = retrofit.baseUrl().toString()

}
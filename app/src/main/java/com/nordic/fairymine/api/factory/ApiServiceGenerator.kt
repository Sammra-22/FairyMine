package com.nordic.fairymine.api.factory

/**
 * Created by Sam22 on 1/6/18.
 */
interface ApiServiceGenerator{
    fun <S> createApiService(serviceClass: Class<S>): S
    val currentHost: String
}
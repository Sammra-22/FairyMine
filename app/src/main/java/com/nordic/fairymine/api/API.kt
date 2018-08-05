package com.nordic.fairymine.api

import com.nordic.fairymine.api.service.CustomerApiService
import com.nordic.fairymine.api.factory.ApiServiceGenerator
import com.nordic.fairymine.api.service.RegistrationApiService

/**
 * Created by Sam22 on 1/6/18.
 */
class API(private val factory: ApiServiceGenerator) {

    val customerApiService: CustomerApiService
        get() = CustomerApiService(factory)

    val registrationApiService: RegistrationApiService
        get() = RegistrationApiService(factory)
}
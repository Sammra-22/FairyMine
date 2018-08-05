package com.nordic.fairymine.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Sam22 on 2/24/18.
 */
class AuthorizationInterceptor(private val apiKey: String): Interceptor{

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val updatedRequest = chain.request()
                .newBuilder()
                .header("Authorization", "Basic $apiKey")
                .build()
        return chain.proceed(updatedRequest)
    }
}
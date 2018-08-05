package com.nordic.fairymine.api.model

/**
 * Created by Sam22 on 2/20/18.
 */
data class Customer(
        val id: Long,
        val mail: String? = null,
        val phone: Int? = null,
        val age: Int? = null,
        val country: String? = null,
        val region: String? = null,
        val operator: String? = null,
        val profession: String? = null
)
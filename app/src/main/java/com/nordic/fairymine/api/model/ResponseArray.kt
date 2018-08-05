package com.nordic.fairymine.api.model

/**
 * Created by Sam22 on 2/20/18.
 */
data class ResponseArray<out T>(
        val content: List<T>,
        val size: Int,
        val first: Boolean,
        val last: Boolean,
        val totalElements: Int,
        val numberOfElements: Int,
        val totalPages: Int,
        val number: Int
)
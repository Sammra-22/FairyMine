package com.nordic.fairymine.helper.rules

import okhttp3.mockwebserver.RecordedRequest

/**
 * Created by Sam22 on 4/3/18.
 */
interface UrlMatchRule {
    fun requestMatches(recordedRequest: RecordedRequest): Boolean
    fun httpCode(): Int
    fun hasBody(): Boolean
}

package com.nordic.fairymine.helper.rules

import okhttp3.mockwebserver.RecordedRequest

/**
 * Created by Sam22 on 4/3/18.
 */

class UrlPathRule(private val pattern: String, private val httpCode: Int) : UrlMatchRule {

    override fun requestMatches(recordedRequest: RecordedRequest): Boolean {
        return pattern.equals(recordedRequest.path, ignoreCase = true)
    }

    override fun httpCode(): Int {
        return httpCode
    }

    override fun hasBody(): Boolean {
        return false
    }
}

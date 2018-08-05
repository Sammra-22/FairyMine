package com.nordic.fairymine.helper.rules

import java.util.regex.Pattern

import okhttp3.mockwebserver.RecordedRequest

/**
 * Created by Sam22 on 4/3/18.
 */

class UrlPathMatchRule(private val pattern: Pattern, private val httpCode: Int) : UrlMatchRule {

    override fun requestMatches(recordedRequest: RecordedRequest): Boolean {
        return pattern.matcher(recordedRequest.path).find()
    }

    override fun httpCode(): Int {
        return httpCode
    }

    override fun hasBody(): Boolean {
        return false
    }
}

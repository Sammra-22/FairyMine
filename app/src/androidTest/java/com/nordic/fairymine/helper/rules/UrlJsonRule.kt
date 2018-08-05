package com.nordic.fairymine.helper.rules

import java.net.HttpURLConnection
import java.util.regex.Pattern

import okhttp3.mockwebserver.RecordedRequest

/**
 * Created by Sam22 on 4/3/18.
 */
class UrlJsonRule private constructor(private val rule: UrlMatchRule, val jsonFilename: String?) : UrlMatchRule {

    override fun requestMatches(recordedRequest: RecordedRequest): Boolean {
        return rule.requestMatches(recordedRequest)
    }

    override fun httpCode(): Int {
        return rule.httpCode()
    }

    override fun hasBody(): Boolean {
        return jsonFilename != null && jsonFilename != ""
    }

    companion object {

        fun valueOf(rule: UrlMatchRule, jsonFilename: String): UrlJsonRule {
            return UrlJsonRule(rule, jsonFilename)
        }

        fun regexpUrlRule(regExpPattern: String, httpCode: Int): UrlJsonRule {
            return regexpUrlRule(Pattern.compile(regExpPattern), "", httpCode)
        }

        fun regexpUrlRule(regExpPattern: String, jsonFilename: String): UrlJsonRule {
            return regexpUrlRule(Pattern.compile(regExpPattern), jsonFilename, HttpURLConnection.HTTP_OK)
        }

        fun regexpUrlRule(regExpPattern: String, jsonFilename: String, httpCode: Int): UrlJsonRule {
            return regexpUrlRule(Pattern.compile(regExpPattern), jsonFilename, httpCode)
        }

        fun regexpUrlRule(regExpPattern: Pattern, jsonFilename: String, httpCode: Int): UrlJsonRule {
            return UrlJsonRule(UrlPathMatchRule(regExpPattern, httpCode), jsonFilename)
        }

        fun regexpUrlRule(regExpPattern: Pattern, httpCode: Int): UrlJsonRule {
            return UrlJsonRule(UrlPathMatchRule(regExpPattern, httpCode), "")
        }

        fun simpleUrlRule(urlPath: String, httpCode: Int): UrlJsonRule {
            return UrlJsonRule(UrlPathRule(urlPath, httpCode), "")
        }

        @JvmOverloads
        fun simpleUrlRule(urlPath: String, jsonFilename: String, httpCode: Int = HttpURLConnection.HTTP_OK): UrlJsonRule {
            return UrlJsonRule(UrlPathRule(urlPath, httpCode), jsonFilename)
        }
    }
}

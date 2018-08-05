package com.nordic.fairymine.helper.rules

import android.content.Context
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

/**
 * Created by Sam22 on 4/3/18.
 */
class JsonUrlDispatcher : Dispatcher {
    private var jsonUrlRules: MutableList<UrlJsonRule> = ArrayList()
    private var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    constructor(context: Context, jsonUrlRules: MutableList<UrlJsonRule>) {
        this.jsonUrlRules = jsonUrlRules
        this.context = context
    }

    constructor(vararg jsonUrlRules: UrlJsonRule) {
        this.jsonUrlRules = ArrayList(Arrays.asList(*jsonUrlRules))
    }

    fun updateRules(vararg jsonUrlRules: UrlJsonRule) {
        this.jsonUrlRules.clear()

        for (rule in jsonUrlRules) {
            addRule(rule)
        }
    }

    fun addRule(jsonUrlRule: UrlJsonRule) {
        this.jsonUrlRules.add(jsonUrlRule)
    }

    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {

        return jsonUrlRules
                .firstOrNull { it.requestMatches(request) }
                ?.let { createMockResponse(it) }
                ?: makeMockNotFoundResponse()
    }

    private fun createMockResponse(jsonRule: UrlJsonRule): MockResponse {
        return if (jsonRule.hasBody()) {
            makeMockResponse(context, jsonRule.jsonFilename, jsonRule.httpCode())
        } else {
            makeMockResponseFromHttpCode(jsonRule.httpCode())
        }
    }

    fun makeMockNotFoundResponse(): MockResponse {
        return MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND).setBody("")
    }

    @JvmOverloads
    fun makeMockResponse(context: Context?, fileName: String?, httpCode: Int = HttpURLConnection.HTTP_OK): MockResponse {
        val jsonBody = if (httpCode >= HTTP_BAD_REQUEST) "" else loadJsonFromAsset(context, fileName)
        return MockResponse().setResponseCode(httpCode)
                .setBody(jsonBody)
    }

    fun makeMockResponseFromHttpCode(httpCode: Int): MockResponse {
        return MockResponse().setResponseCode(httpCode).setBody("")
    }

    private fun loadJsonFromAsset(context: Context?, fileName: String?): String {
        return try {
            val `in` = context!!.assets.open(fileName)
            val total = StringBuilder(`in`.available())
            val r = BufferedReader(InputStreamReader(`in`, UTF_8))
            var line: String? = ""
            do {
                total.append(line)
                line = r.readLine()
            } while (line != null)
            total.toString()
        } catch (ex: IOException) {
            ex.printStackTrace()
            ""
        }

    }
}
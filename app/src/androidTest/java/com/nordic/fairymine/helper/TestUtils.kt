package com.nordic.fairymine.helper

import android.support.test.InstrumentationRegistry
import com.nordic.fairymine.MockNordicApplication
import com.nordic.fairymine.MockTestRunner
import com.nordic.fairymine.helper.rules.JsonUrlDispatcher
import okhttp3.mockwebserver.MockWebServer

/**
 * Created by Sam22 on 4/3/18.
 */
class TestUtils {

    val applicationContext: MockNordicApplication
        get() {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            return instrumentation.targetContext.applicationContext as MockNordicApplication
        }

    val dispatcher: JsonUrlDispatcher
        get() {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            return (instrumentation as MockTestRunner).dispatcher
        }

    val azaMockWebServer: MockWebServer
        get() {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            return (instrumentation as MockTestRunner).mockWebServer
        }
}
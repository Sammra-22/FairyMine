package com.nordic.fairymine.common.utils

import android.util.Log
import com.nordic.fairymine.BuildConfig

/**
 * Created by Sam22 on 2/20/18.
 */
object KotLog{

    private val DEBUG = BuildConfig.DEBUG

    fun i(tag: String, string: String) {
        if (DEBUG) {
            Log.i(tag, string)
        }
    }

    fun e(tag: String, string: String) {
        if (DEBUG) {
            Log.e(tag, string)
        }
    }

    fun d(tag: String, string: String) {
        if (DEBUG) {
            Log.d(tag, string)
        }
    }

    fun v(tag: String, string: String) {
        if (DEBUG) {
            Log.v(tag, string)
        }
    }

    fun w(tag: String, string: String) {
        if (DEBUG) {
            Log.w(tag, string)
        }
    }
}
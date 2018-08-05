package com.nordic.fairymine.api.gson

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nordic.fairymine.api.model.User

/**
 * Created by Sam22 on 2/20/18.
 */
object GsonFactory {

    fun createGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        registerTypeAdapters(gsonBuilder)
        return gsonBuilder.create()
    }

    private fun registerTypeAdapters(builder: GsonBuilder) {
        builder.registerTypeAdapter(User::class.java, UserConverter())
    }

}
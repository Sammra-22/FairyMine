package com.nordic.fairymine.dagger.module

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nordic.fairymine.api.API
import com.nordic.fairymine.api.factory.ApiServiceGenerator
import com.nordic.fairymine.api.factory.RetrofitServiceGenerator
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Sam22 on 2/3/18.
 */
@Module
class ApiModule(private val baseUrl: String) {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

    @Provides
    @Singleton
    fun provideApiServiceGenerator(retrofit: Retrofit): ApiServiceGenerator = RetrofitServiceGenerator(retrofit)


    @Provides
    @Singleton
    fun provideApi(apiServiceGenerator: ApiServiceGenerator): API = API(apiServiceGenerator)

}
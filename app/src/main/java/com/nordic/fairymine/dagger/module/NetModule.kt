package com.nordic.fairymine.dagger.module

import android.content.Context
import android.os.Build
import com.google.gson.Gson
import com.nordic.fairymine.BuildConfig
import com.nordic.fairymine.R
import com.nordic.fairymine.api.gson.GsonFactory
import com.nordic.fairymine.api.interceptor.AuthorizationInterceptor
import com.nordic.fairymine.api.interceptor.ContentTypeInterceptor
import com.nordic.fairymine.api.interceptor.UserAgentInterceptor
import com.nordic.fairymine.dagger.AppTarget
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


/**
 * Created by Sam22 on 2/3/18.
 */
@Module
class NetModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(@AppTarget context: Context): Cache {
        val cacheSize = 1024 * 1024 // 1 MB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonFactory.createGson()
    }

    @Provides
    @Singleton
    internal fun providesUserAgentInterceptor(@AppTarget context: Context): UserAgentInterceptor {
        val userAgent = context.getString(R.string.user_agent,
                context.packageName,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                Build.VERSION.RELEASE)

        return UserAgentInterceptor(userAgent)
    }

    @Provides
    @Singleton
    internal fun providesContentTypeInterceptor(): ContentTypeInterceptor {
        return ContentTypeInterceptor("application/json")
    }

    @Provides
    @Singleton
    internal fun providesAuthorizationInterceptor(): AuthorizationInterceptor {
        return AuthorizationInterceptor("YXBpOmZhaXJ5bWluZS1BUEktcGFzcw==")
    }

    @Provides
    @Singleton
    internal fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, userAgentInterceptor: UserAgentInterceptor,
                            contentTypeInterceptor: ContentTypeInterceptor,
                            authorizationInterceptor: AuthorizationInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder().cache(cache)
                .addNetworkInterceptor(userAgentInterceptor)
                .addNetworkInterceptor(contentTypeInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build()

    }

}
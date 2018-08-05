package se.avanzabank.androidapplikation.dagger

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
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * CCreated by Sam22 on 4/3/18.
 */
@Module
class TestNetModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(@AppTarget context: Context): Cache = Cache(context.cacheDir, 0)

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
        return AuthorizationInterceptor("")
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, userAgentInterceptor: UserAgentInterceptor,
                            contentTypeInterceptor: ContentTypeInterceptor,
                            authorizationInterceptor: AuthorizationInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cookieJar(CookieJar.NO_COOKIES)
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        client.apply {
            cache(cache)
            addNetworkInterceptor(userAgentInterceptor)
            addNetworkInterceptor(contentTypeInterceptor)
            addNetworkInterceptor(authorizationInterceptor)
            addInterceptor(httpLoggingInterceptor)
        }
        return client.build()
    }

}
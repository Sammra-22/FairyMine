package se.avanzabank.androidapplikation.dagger

import android.content.Context
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nordic.fairymine.api.API
import com.nordic.fairymine.api.factory.ApiServiceGenerator
import com.nordic.fairymine.api.factory.RetrofitServiceGenerator
import com.nordic.fairymine.dagger.AppTarget
import com.nordic.fairymine.helper.rules.JsonUrlDispatcher
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Single
import rx.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Created by Sam22 on 4/3/18.
 */
@Module
class TestApiModule {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl("https://localhost")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(io.reactivex.schedulers.Schedulers.io()))
            .build()

    @Provides
    @Singleton
    internal fun providesApiServiceFactory(retrofit: Retrofit): ApiServiceGenerator = RetrofitServiceGenerator(retrofit)


    @Provides
    @Singleton
    internal fun providesApi(apiServiceFactory: ApiServiceGenerator): API = API(apiServiceFactory)


    @Provides
    @Singleton
    internal fun providesJsonUrlDispatcher(@AppTarget context: Context): JsonUrlDispatcher = JsonUrlDispatcher(context)

    @Provides
    @Singleton
    internal fun providesMockWebServer(dispatcher: JsonUrlDispatcher): MockWebServer {
        val mockWebServer = MockWebServer()
        mockWebServer.setDispatcher(dispatcher)
        return mockWebServer
    }

    @Provides
    @Singleton
    internal fun providesUrl(server: MockWebServer): HttpUrl {
        return Single.fromCallable({
            server.start()
            server.url("/")
        }).subscribeOn(Schedulers.io())
                .doOnError { it.printStackTrace() }
                .toBlocking()
                .value()
    }


}

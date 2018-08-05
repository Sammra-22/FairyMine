package com.nordic.fairymine

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import com.nordic.fairymine.dagger.DaggerTestAppComponent
import com.nordic.fairymine.dagger.module.AppModule
import com.nordic.fairymine.helper.rules.JsonUrlDispatcher
import okhttp3.mockwebserver.MockWebServer
import rx.Single
import rx.schedulers.Schedulers
import se.avanzabank.androidapplikation.dagger.TestApiModule
import se.avanzabank.androidapplikation.dagger.TestNetModule
import javax.inject.Inject

/**
 * Created by Sam22 on 4/3/18.
 */
class MockTestRunner : AndroidJUnitRunner() {

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var dispatcher: JsonUrlDispatcher


    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        val application = super.newApplication(cl, MockNordicApplication::class.java.name, context) as MockNordicApplication
        val component = DaggerTestAppComponent.builder()
                .appModule(AppModule(context))
                .testNetModule(TestNetModule())
                .testApiModule(TestApiModule())
                .build()

        component.inject(this)
        return application
    }

    override fun onDestroy() {
        super.onDestroy()
        mockWebServer.let {
            Single.fromCallable<Any>({
                it.close()
                null
            }).subscribeOn(Schedulers.io())
                    .doOnError { it.printStackTrace() }
                    .toBlocking().value()
        }

    }
}
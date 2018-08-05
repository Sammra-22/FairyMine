package com.nordic.fairymine.dagger

import android.content.Context
import com.nordic.fairymine.MockNordicApplication
import com.nordic.fairymine.MockTestRunner
import com.nordic.fairymine.dagger.module.ActivityModule
import com.nordic.fairymine.dagger.module.AppModule
import com.nordic.fairymine.dagger.module.FragmentModule
import dagger.Component
import se.avanzabank.androidapplikation.dagger.TestApiModule
import se.avanzabank.androidapplikation.dagger.TestNetModule
import javax.inject.Singleton

/**
 * Created by Sam22 on 4/3/18.
 */
@Singleton
@Component(modules = [
    AppModule::class,
    TestNetModule::class,
    TestApiModule::class,
    ActivityModule::class,
    FragmentModule::class
])
interface TestAppComponent : AppComponent {

    fun inject(app: MockNordicApplication)
    fun inject(testRunner: MockTestRunner)

    companion object {

        fun init(context: Context): TestAppComponent = DaggerTestAppComponent.builder()
                .appModule(AppModule(context))
                .testNetModule(TestNetModule())
                .testApiModule(TestApiModule())
                .build()
    }
}
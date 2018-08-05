package com.nordic.fairymine.dagger

import android.content.Context
import com.nordic.fairymine.NordicApplication
import com.nordic.fairymine.dagger.module.*
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Sam22 on 2/3/18.
 */
@Singleton
@Component(modules = [
    AppModule::class,
    NetModule::class,
    ApiModule::class,
    ActivityModule::class,
    FragmentModule::class
])
interface AppComponent {

    // Application
    fun inject(app: NordicApplication)

    companion object {
        private const val API_HOST = "https://nordicit-api.herokuapp.com/"

        fun init(context: Context): AppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(context))
                .netModule(NetModule())
                .apiModule(ApiModule(API_HOST))
                .build()
    }
}
package com.nordic.fairymine

import android.app.Activity
import android.app.Application
import com.nordic.fairymine.dagger.AppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


/**
 * Created by Sam22 on 1/28/18.
 */
open class NordicApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    open val appComponent: AppComponent by lazy { AppComponent.init(this) }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
}
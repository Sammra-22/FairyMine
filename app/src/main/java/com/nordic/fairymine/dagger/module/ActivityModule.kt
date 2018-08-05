package com.nordic.fairymine.dagger.module

import com.nordic.fairymine.MainActivity
import com.nordic.fairymine.about.SimpleInfoActivity
import com.nordic.fairymine.RegisterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Sam22 on 2/3/18.
 */
@Module
abstract class ActivityModule{

    @ContributesAndroidInjector
    internal abstract fun registerActivity(): RegisterActivity

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun InfoActivity(): SimpleInfoActivity

}
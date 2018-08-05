package com.nordic.fairymine.dagger.module

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.core.sdk.AdModule
import com.core.sdk.model.AdPolicy
import com.nordic.fairymine.common.storage.AppSettings
import com.nordic.fairymine.dagger.AppTarget
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Sam22 on 2/3/18.
 */
@Module
class AppModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    @AppTarget
    internal fun provideApplicationContext(): Context = applicationContext

    @Provides
    @Singleton
    fun providesSharedPreferences(@AppTarget context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun providesAppSettings(sharedPreferences: SharedPreferences): AppSettings =
            AppSettings(sharedPreferences)

    @Provides
    @Singleton
    fun providesAdModule(@AppTarget context: Context): AdModule =
            AdModule(context, AdPolicy())

}
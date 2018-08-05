package com.nordic.fairymine.common.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.nordic.fairymine.MainActivity
import com.nordic.fairymine.RegisterActivity
import com.nordic.fairymine.api.API
import com.nordic.fairymine.api.model.Customer
import com.nordic.fairymine.common.model.CycleEvent
import com.nordic.fairymine.common.storage.AppSettings
import com.nordic.fairymine.common.storage.PersistedSettingsKey
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * Created by Sam22 on 12/21/17.
 */
open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    protected lateinit var settings: AppSettings

    @Inject
    protected lateinit var api: API

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    val customer by lazy { Customer(settings.readSetting(PersistedSettingsKey.CUSTOMER_ACCOUNT_ID)) }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    private val activityCycleSubject = BehaviorSubject.create<CycleEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        if (this !is RegisterActivity) {
            checkRegistrationStatus()
        }
    }

    override fun onStart() {
        super.onStart()
        activityCycleSubject.onNext(CycleEvent.START)
    }

    override fun onStop() {
        activityCycleSubject.onNext(CycleEvent.STOP)
        super.onStop()
    }

    override fun onDestroy() {
        activityCycleSubject.onComplete()
        super.onDestroy()
    }

    private fun checkRegistrationStatus() {
        val persistedAccountId = settings.readSetting<Int>(PersistedSettingsKey.CUSTOMER_ACCOUNT_ID)
        if (persistedAccountId < 0) {
            launchRegistration()
        }
    }

    protected fun launchMain() = startActivity(Intent(this, MainActivity::class.java)).also { finish() }

    private fun launchRegistration() = startActivity(Intent(this, RegisterActivity::class.java)).also { finish() }

    protected fun getLifecycleEvents(vararg events: CycleEvent): Flowable<CycleEvent> = activityCycleSubject.filter {
        events.any { activityEvent -> it == activityEvent }
    }.toFlowable(BackpressureStrategy.LATEST)

}
package com.nordic.fairymine.common.view

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.nordic.fairymine.api.API
import com.nordic.fairymine.api.model.Customer
import com.nordic.fairymine.common.model.CycleEvent
import com.nordic.fairymine.common.storage.AppSettings
import com.nordic.fairymine.common.storage.PersistedSettingsKey
import dagger.android.support.AndroidSupportInjection
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * Created by Sam22 on 1/7/18.
 */
abstract class BaseFragment : Fragment() {

    @Inject
    protected lateinit var api: API

    @Inject
    protected lateinit var settings: AppSettings

    private val fragmentCycleSubject = BehaviorSubject.create<CycleEvent>()

    val customer by lazy { Customer(settings.readSetting(PersistedSettingsKey.CUSTOMER_ACCOUNT_ID)) }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        onAttachContext(context)
    }

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun onAttach(activity: Activity?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(activity)
        onAttachContext(activity)
    }


    open fun onAttachContext(context: Context?) {}

    abstract fun loadData(): Disposable

    fun loadData(api: API, settings: AppSettings) {
        this.api = api
        this.settings = settings
        loadData()
    }

    override fun onStart() {
        super.onStart()
        fragmentCycleSubject.onNext(CycleEvent.START)
        loadData()
    }

    override fun onStop() {
        fragmentCycleSubject.onNext(CycleEvent.STOP)
        super.onStop()
    }

    protected fun getLifecycleEvents(vararg events: CycleEvent): Flowable<CycleEvent> = fragmentCycleSubject.filter {
        events.any { activityEvent -> it == activityEvent }
    }.toFlowable(BackpressureStrategy.LATEST)


    companion object {
        private const val EXTRA_CUSTOMER = "EXTRA_CUSTOMER"
    }
}
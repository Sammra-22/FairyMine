package com.nordic.fairymine

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.core.sdk.AdModule
import com.core.sdk.AdPlaybackListener
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.nordic.fairymine.about.SimpleInfoActivity
import com.nordic.fairymine.account.HoldingFragment
import com.nordic.fairymine.api.model.Offer
import com.nordic.fairymine.common.adapter.AppPagerAdapter
import com.nordic.fairymine.common.model.CycleEvent
import com.nordic.fairymine.common.model.ProgressInstance
import com.nordic.fairymine.common.storage.PersistedSettingsKey
import com.nordic.fairymine.common.storage.model.CachedOffers
import com.nordic.fairymine.common.storage.model.PendingOrders
import com.nordic.fairymine.common.view.BaseActivity
import com.nordic.fairymine.common.view.BaseFragment
import com.nordic.fairymine.common.view.ProgressDialogFragment
import com.nordic.fairymine.common.view.StatusDialogFragment
import com.nordic.fairymine.databinding.ActivityMainBinding
import com.nordic.fairymine.product.OffersFragment
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity(), Toolbar.OnMenuItemClickListener, AdPlaybackListener, OrderInterface {

    @Inject
    lateinit var adModule: AdModule

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: AppPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.toolbar.apply {
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener(this@MainActivity)
        }
        initViewPager()
        adModule.setPlaybackListener(this)
    }

    private fun initViewPager() {
        viewPagerAdapter = AppPagerAdapter(supportFragmentManager)
                .apply {
                    addFragment(HoldingFragment.newInstance(),
                            getString(R.string.main_tab_account))
                    addFragment(OffersFragment.newInstance(),
                            getString(R.string.main_tab_offers))
                }
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 2
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.menu_main_about -> {
                    startActivity(SimpleInfoActivity.newIntent(this,
                            getString(R.string.about_title),
                            getString(R.string.about_body)))
                    true
                }
                R.id.menu_main_contact -> {
                    startActivity(SimpleInfoActivity.newIntent(this,
                            getString(R.string.contact_title),
                            getString(R.string.contact_body),
                            resources.getStringArray(R.array.contact_highlights),
                            resources.getStringArray(R.array.contact_links)))
                    true
                }
                R.id.menu_main_terms -> {
                    startActivity(SimpleInfoActivity.newIntent(this,
                            getString(R.string.terms_title),
                            getString(R.string.terms_body)))
                    true
                }
                else -> false
            }

    override fun initiateAdOrder(offer: Offer): Boolean {
        ProgressDialogFragment.show(fragmentManager, getString(R.string.offers_ads_loading_progress))
        val offerProgressValue = offer.progress?.value ?: 0
        api.customerApiService.getAdSegment(customer, offer)
                .takeUntil(getLifecycleEvents(CycleEvent.STOP))
                .subscribe({
                    adModule.renderAds(offer.unitValue - offerProgressValue, offer.id)
                }, {
                    ProgressDialogFragment.dismiss()
                })
        return true
    }

    override fun sendOrder(offerId: Long) {
        ProgressDialogFragment.show(fragmentManager, getString(R.string.dialog_order_progress), false)
        var orderSuccess = false
        api.customerApiService.sendOrder(customer, offerId)
                .takeUntil(getLifecycleEvents(CycleEvent.STOP))
                .doFinally {
                    ProgressDialogFragment.dismiss()
                    if (!orderSuccess) {
                        persistPendingOrder(offerId)
                    }
                }
                .doAfterTerminate({
                    viewPagerAdapter.getAllFragments().forEach {
                        (it as? BaseFragment)?.loadData(api, settings)
                    }
                })
                .subscribe({
                    orderSuccess = true
                    showVoucherOrderSuccess()
                }, {
                    val errorCode = (it as? HttpException)?.code() ?: NETWORK_ERROR
                    showVoucherOrderFailure(errorCode)
                })
    }

    override fun persistPendingOrder(offerId: Long, progress: ProgressInstance?) {
        val cachedOffers = settings.readSetting<CachedOffers>(PersistedSettingsKey.CACHED_OFFERS)
        val pendingOffer = cachedOffers.offers.firstOrNull { offer -> offer.id == offerId }
        pendingOffer?.run {
            this.progress = progress
            val pendingOrders = settings.readSetting<PendingOrders>(PersistedSettingsKey.CUSTOMER_PENDING_ORDERS)
            pendingOrders.offers.add(this)
            settings.storeSetting(PersistedSettingsKey.CUSTOMER_PENDING_ORDERS, pendingOrders)
        }
    }

    override fun discardPendingOrder(offerId: Long, progress: ProgressInstance?) {
        val pendingOrders = settings.readSetting<PendingOrders>(PersistedSettingsKey.CUSTOMER_PENDING_ORDERS)
        pendingOrders.offers.firstOrNull { it.id == offerId && it.progress?.value == progress?.value }?.apply {
            pendingOrders.offers.remove(this)
            settings.storeSetting(PersistedSettingsKey.CUSTOMER_PENDING_ORDERS, pendingOrders)
        }
    }

    override fun onAdPlaybackCompleted(offerId: Long) = sendOrder(offerId)

    override fun onAdPlaybackInterrupted(offerId: Long, watchCount: Int) {
        if (watchCount > 0) {
            persistPendingOrder(offerId, ProgressInstance(Date().time, watchCount))
        }
    }

    private fun showVoucherOrderSuccess() = StatusDialogFragment.show(fragmentManager,
            getString(R.string.dialog_order_success_title),
            getString(R.string.dialog_order_success_body), true)

    private fun showVoucherOrderFailure(errorCode: Int) = when (errorCode) {
        ALLOCATION_ERROR -> StatusDialogFragment.show(fragmentManager,
                getString(R.string.dialog_order_failure_title),
                getString(R.string.dialog_order_failure_body), false)
        else -> StatusDialogFragment.show(fragmentManager,
                getString(R.string.dialog_order_error_network_title),
                getString(R.string.dialog_order_error_network_body), false)

    }

    companion object {
        const val ALLOCATION_ERROR = 404
        const val NETWORK_ERROR = 408
    }


}

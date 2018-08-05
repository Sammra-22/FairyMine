package com.nordic.fairymine.product

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nordic.fairymine.OrderInterface
import com.nordic.fairymine.api.model.Offer
import com.nordic.fairymine.common.storage.PersistedSettingsKey
import com.nordic.fairymine.common.storage.model.CachedOffers
import com.nordic.fairymine.common.view.ActionDialogFragment
import com.nordic.fairymine.common.view.BaseFragment
import com.nordic.fairymine.common.model.CycleEvent
import com.nordic.fairymine.databinding.FragmentOfferingsBinding
import com.nordic.fairymine.product.adapter.AdOfferListAdapter
import com.nordic.fairymine.product.listener.AdOfferActionListener
import io.reactivex.disposables.Disposable

/**
 * Created by Sam22 on 1/7/18.
 */
class OffersFragment : BaseFragment(), AdOfferActionListener {

    private lateinit var binding: FragmentOfferingsBinding
    private val adapter = AdOfferListAdapter(this)
    private lateinit var orderListener: OrderInterface

    override fun onAttachContext(context: Context?) {
        super.onAttachContext(context)
        orderListener = context as OrderInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            FragmentOfferingsBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.offeringsAdProductList.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = this@OffersFragment.adapter
        }
    }

    override fun loadData(): Disposable = api.customerApiService.getAllOffers()
            .takeUntil(getLifecycleEvents(CycleEvent.STOP))
            .subscribe({
                updateAdOffers(it.content)
                settings.storeSetting(PersistedSettingsKey.CACHED_OFFERS, CachedOffers(ArrayList(it.content)))
            }, {
                adapter.setError()
            })


    override fun onReloadAdOffers() {
        adapter.setProgress()
        loadData()
    }

    private fun updateAdOffers(items: List<Offer>) {
        binding.offeringsAdProductList.apply {
            layoutManager = if (items.isEmpty() || items.size % 2 != 0)
                LinearLayoutManager(context)
            else
                GridLayoutManager(context, 2)
        }
        adapter.setItems(items)
    }

    override fun onAdOfferDetail(offer: Offer) =
            ActionDialogFragment.show(fragmentManager, offer.name, offer.description)


    override fun onAdOfferSelected(offer: Offer): Boolean = orderListener.initiateAdOrder(offer)

    companion object {
        fun newInstance(): OffersFragment = OffersFragment()
    }

}
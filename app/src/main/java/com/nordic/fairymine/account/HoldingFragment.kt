package com.nordic.fairymine.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nordic.fairymine.OrderInterface
import com.nordic.fairymine.R
import com.nordic.fairymine.account.adapter.InterruptedOrdersAdapter
import com.nordic.fairymine.account.adapter.PendingOrdersAdapter
import com.nordic.fairymine.account.adapter.VoucherListAdapter
import com.nordic.fairymine.account.listener.PendingOrderListener
import com.nordic.fairymine.account.listener.VoucherActionListener
import com.nordic.fairymine.api.model.Offer
import com.nordic.fairymine.api.model.Voucher
import com.nordic.fairymine.common.model.CycleEvent
import com.nordic.fairymine.common.storage.PersistedSettingsKey
import com.nordic.fairymine.common.storage.model.CachedVouchers
import com.nordic.fairymine.common.storage.model.PendingOrders
import com.nordic.fairymine.common.view.ActionDialogFragment
import com.nordic.fairymine.common.view.BaseFragment
import com.nordic.fairymine.common.view.widgets.VerticalListDivider
import com.nordic.fairymine.databinding.FragmentAccountBinding
import io.reactivex.disposables.Disposable


/**
 * Created by Sam22 on 1/7/18.
 */
class HoldingFragment : BaseFragment(), VoucherActionListener, PendingOrderListener {

    private lateinit var binding: FragmentAccountBinding
    private val voucherListAdapter = VoucherListAdapter(this)
    private val pendingOrdersAdapter = PendingOrdersAdapter(this)
    private val interruptedOrdersAdapter = InterruptedOrdersAdapter(this)
    private lateinit var orderListener: OrderInterface

    override fun onAttachContext(context: Context?) {
        super.onAttachContext(context)
        orderListener = context as OrderInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            FragmentAccountBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            arrayListOf(accountVouchersList,
                    accountPendingOrdersList,
                    accountInterruptedOrdersList).forEach {
                it.layoutManager = LinearLayoutManager(view.context)
                it.addItemDecoration(VerticalListDivider(view.context))
            }
            accountVouchersList.adapter = voucherListAdapter
            accountPendingOrdersList.adapter = pendingOrdersAdapter
            accountInterruptedOrdersList.adapter = interruptedOrdersAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        val pendingOrders = settings.readSetting<PendingOrders>(PersistedSettingsKey.CUSTOMER_PENDING_ORDERS)
        updatePendingOrders(pendingOrders.offers)
        settings.streamReadSetting<PendingOrders>(PersistedSettingsKey.CUSTOMER_PENDING_ORDERS)
                .subscribe({
                    updatePendingOrders(it.offers)
                })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isAdded) {
            val cachedVouchers = settings.readSetting<CachedVouchers>(PersistedSettingsKey.CACHED_VOUCHERS)
            if (cachedVouchers.vouchers.isNotEmpty()) {
                voucherListAdapter.setItems(cachedVouchers.vouchers)
            }
        }
    }

    private fun updatePendingOrders(persisted: List<Offer>) {
        val pending = persisted.filter { it.progress == null }
        val interrupted = persisted.filter { it.progress != null }
        binding.accountCardPending.visibility = if (pending.isEmpty()) View.GONE else View.VISIBLE
        pendingOrdersAdapter.setItems(pending)
        binding.accountCardInterrupted.visibility = if (interrupted.isEmpty()) View.GONE else View.VISIBLE
        interruptedOrdersAdapter.setItems(interrupted)
    }

    override fun loadData(): Disposable = api.customerApiService.getVouchers(customer)
            .takeUntil(getLifecycleEvents(CycleEvent.STOP))
            .subscribe({
                settings.storeSetting(PersistedSettingsKey.CACHED_VOUCHERS, it.content)
                voucherListAdapter.setItems(it.content)
            }) {
                it.printStackTrace()
                if (voucherListAdapter.itemCount == 0) {
                    voucherListAdapter.setError()
                }
            }

    override fun onReloadVouchers() {
        voucherListAdapter.setProgress()
        loadData()
    }

    override fun onDisplayVoucherCode(voucher: Voucher) {
        ActionDialogFragment.show(fragmentManager,
                String.format(getString(R.string.dialog_account_voucher_code_title), voucher.pin),
                getString(R.string.dialog_account_voucher_code_body),
                getString(R.string.action_cancel),
                getString(R.string.action_dial)) { dialVoucherCode(voucher.pin) }
    }

    private fun dialVoucherCode(code: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code)))
        startActivity(intent)
    }

    override fun onResendPendingTransaction(offer: Offer) = with(orderListener) {
        discardPendingOrder(offer.id)
        sendOrder(offer.id)
    }

    override fun onResumeInterruptedOrder(offer: Offer) = with(orderListener) {
        discardPendingOrder(offer.id, offer.progress)
        initiateAdOrder(offer)
    }

    companion object {
        fun newInstance(): HoldingFragment = HoldingFragment()
    }

}
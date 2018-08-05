package com.nordic.fairymine

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.nordic.fairymine.api.model.Customer
import com.nordic.fairymine.api.model.ResponseArray
import com.nordic.fairymine.api.model.User
import com.nordic.fairymine.api.model.User.Companion.COUNTRY_KEY
import com.nordic.fairymine.common.storage.PersistedSettingsKey
import com.nordic.fairymine.common.utils.KotLog
import com.nordic.fairymine.common.utils.hideKeyboard
import com.nordic.fairymine.common.view.ActionDialogFragment
import com.nordic.fairymine.common.view.BaseActivity
import com.nordic.fairymine.common.model.CycleEvent
import com.nordic.fairymine.databinding.ActivityRegisterBinding


/**
 * Created by Sam22 on 12/21/17.
 */
class RegisterActivity : BaseActivity() {

    enum class ScreenId { PROGRESS, SIGN_IN, SUBSCRIBER_FORM, REGISTERER_FORM }

    private lateinit var dataBinding: ActivityRegisterBinding

    private val user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        dataBinding.signInButton.apply {
            setSize(SignInButton.SIZE_WIDE)
            setColorScheme(SignInButton.COLOR_DARK)
            setOnClickListener { onSignInClick() }
        }
        initRegistrationForm()
    }

    private fun initRegistrationForm() = with(dataBinding) {
        registrationSubscriberForm.setFormButtonText(R.string.action_next)
        registrationSubscriberForm.setOnFormValidated {
            root.hideKeyboard()
            displayView(ScreenId.PROGRESS)
            user.subscriptionForm.getInfoRowForKey(COUNTRY_KEY)?.let {
                loadRegistrationForm(it.value.get()!!.toLowerCase())
            }
        }
        registrationUserForm.setFormButtonText(R.string.action_register)
        registrationUserForm.setOnFormValidated {
            displayView(ScreenId.PROGRESS)
            register()
        }
    }

    override fun onStart() {
        super.onStart()
        val signedInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (signedInAccount != null) {
            user.mail = signedInAccount.email
            authenticate()
        } else {
            displayView(ScreenId.SIGN_IN)
        }
    }

    private fun authenticate() {
        displayView(ScreenId.PROGRESS)
        api.customerApiService.authenticateCustomer(user)
                .takeUntil(getLifecycleEvents(CycleEvent.STOP))
                .subscribe({
                    if (!handleAuthenticationResponse(it)) {
                        with(user) {
                            when {
                                registrationForm.isLoaded -> displayView(ScreenId.REGISTERER_FORM)
                                subscriptionForm.isLoaded -> displayView(ScreenId.SUBSCRIBER_FORM)
                                else -> loadCountries()
                            }
                        }
                    }
                }, {
                    it.printStackTrace()
                    displayView(ScreenId.SIGN_IN)
                    displayAuthenticationError()
                })
    }

    private fun register() {
        api.registrationApiService.registerCustomer(user.withDevice(this))
                .andThen(api.customerApiService.authenticateCustomer(user))
                .takeUntil(getLifecycleEvents(CycleEvent.STOP))
                .subscribe({
                    if (!handleAuthenticationResponse(it)) {
                        displayView(ScreenId.REGISTERER_FORM)
                        displayGenericError()
                    }
                }, {
                    it.printStackTrace()
                    displayView(ScreenId.REGISTERER_FORM)
                    displayGenericError()
                })
    }

    private fun handleAuthenticationResponse(response: ResponseArray<Customer>): Boolean {
        if (response.content.isNotEmpty()) {
            settings.storeSetting(PersistedSettingsKey.CUSTOMER_ACCOUNT_ID, response.content[0].id)
            launchMain()
            return true
        }
        return false
    }

    private fun onSignInClick() {
        displayView(ScreenId.PROGRESS)
        startActivityForResult(GoogleSignIn.getClient(this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()).signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {
        val account = completedTask.getResult(ApiException::class.java)
        user.mail = account.email
        authenticate()
    } catch (e: ApiException) {
        KotLog.w(TAG, "signInResult:failed code=" + e.statusCode)
        displayView(ScreenId.SIGN_IN)
    }

    private fun displayView(screen: ScreenId) {
        dataBinding.registrationViewFlipper.apply {
            displayedChild = when (screen) {
                ScreenId.PROGRESS -> indexOfChild(dataBinding.progress)
                ScreenId.SIGN_IN -> indexOfChild(dataBinding.registrationSignIn)
                ScreenId.SUBSCRIBER_FORM -> indexOfChild(dataBinding.registrationSubscriberForm)
                ScreenId.REGISTERER_FORM -> indexOfChild(dataBinding.registrationUserForm)
            }
        }
    }

    private fun loadCountries() {
        api.registrationApiService.getCountryList()
                .takeUntil(getLifecycleEvents(CycleEvent.STOP))
                .subscribe({
                    val countiesMap = it.associateBy({ it.toLowerCase().capitalize() }, { it })
                    user.populateSubscriptionForm(this, countiesMap)
                    dataBinding.registrationSubscriberForm.setFormData(user.subscriptionForm)
                    displayView(ScreenId.SUBSCRIBER_FORM)
                }, {
                    displayView(ScreenId.SIGN_IN)
                    displayGenericError()
                })
    }

    private fun loadRegistrationForm(country: String) {
        api.registrationApiService.getUserForm(country)
                .takeUntil(getLifecycleEvents(CycleEvent.STOP))
                .subscribe({
                    user.populateRegistrationForm(it.registrationForm)
                    dataBinding.registrationUserForm.setFormData(user.registrationForm)
                    displayView(ScreenId.REGISTERER_FORM)
                }, {
                    displayView(ScreenId.SUBSCRIBER_FORM)
                    displayGenericError()
                })
    }

    private fun displayGenericError() {
        ActionDialogFragment.show(supportFragmentManager,
                getString(R.string.dialog_register_error_title),
                getString(R.string.dialog_register_error_body))
    }

    private fun displayAuthenticationError() {
        ActionDialogFragment.show(supportFragmentManager,
                getString(R.string.dialog_authenticate_error_title),
                getString(R.string.dialog_authenticate_error_body),
                getString(R.string.dialog_authenticate_btn_cancel),
                getString(R.string.dialog_authenticate_btn_retry),
                this::authenticate)
    }

    companion object {
        const val GOOGLE_SIGN_IN = 1
        val TAG = RegisterActivity::class.java.name!!
    }
}
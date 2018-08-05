package com.nordic.fairymine.api.model

import android.content.Context
import android.os.Build
import android.text.InputType
import com.nordic.fairymine.R
import com.nordic.fairymine.common.form.Form
import com.nordic.fairymine.common.form.InfoRow
import com.nordic.fairymine.common.form.InfoRowType


/**
 * Created by Sam22 on 3/6/18.
 */
data class User(var mail: String? = null,
                val subscriptionForm: Form = Form(),
                val registrationForm: Form = Form()) {


    fun populateSubscriptionForm(context: Context, countriesMap: Map<String, String>) {
        subscriptionForm.updateInfo(arrayListOf(
                InfoRow(COUNTRY_KEY, context.getString(R.string.label_country), InfoRowType.SingleSelect(countriesMap)),
                InfoRow(AGE_KEY, context.getString(R.string.label_age), InfoRowType.TextInput(InputType.TYPE_CLASS_NUMBER)),
                InfoRow(PHONE_KEY, context.getString(R.string.label_phone_number), InfoRowType.TextInput(InputType.TYPE_CLASS_PHONE)))
        )
    }

    fun populateRegistrationForm(form: Form) {
        registrationForm.updateInfo(form.infoList)
    }

    fun withDevice(context: Context): User {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val infoRow = InfoRow(DEVICE_KEY, context.getString(R.string.label_device), InfoRowType.TextInput())
        infoRow.value.set(
                if (model.startsWith(manufacturer)) {
                    model.capitalize()
                } else {
                    manufacturer.capitalize() + " " + model
                })
        return copy(registrationForm = registrationForm.accumulate(infoRow))
    }

    companion object {
        const val COUNTRY_KEY = "country"
        private const val AGE_KEY = "age"
        private const val PHONE_KEY = "phone"
        private const val DEVICE_KEY = "device"
    }
}
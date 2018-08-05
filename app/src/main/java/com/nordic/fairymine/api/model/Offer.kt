package com.nordic.fairymine.api.model

import com.nordic.fairymine.common.model.ProgressInstance
import com.nordic.fairymine.common.model.ValueUnit

/**
 * Created by Sam22 on 2/26/18.
 */
data class Offer(
        val id: Long,
        val name: String,
        val description: String,
        val category: Type,
        val unitValue: Int,
        override val dataValue: Double,
        override val dataUnit: String
) : ValueUnit() {

    enum class Type { AD_OFFER, SURVEY_OFFER }

    var progress: ProgressInstance? = null

}
package com.nordic.fairymine.common.form

/**
 * Created by Sam22 on 12/21/17.
 */
open class Form(val infoList: ArrayList<InfoRow> = arrayListOf()) {

    val isValid: Boolean
        get() {
            infoList.forEach {
                it.hasError.set(it.value.get().isNullOrBlank())
            }
            return infoList.isNotEmpty() && infoList.none { it.hasError.get() }
        }

    val isLoaded: Boolean
        get() = infoList.isNotEmpty()

    fun updateInfo(infoList: ArrayList<InfoRow>) {
        this.infoList.clear()
        this.infoList.addAll(infoList)
    }

    open fun accumulate(infoRow: InfoRow): Form {
        val form = Form()
        form.infoList.addAll(infoList)
        form.infoList.add(infoRow)
        return form
    }

    fun getInfoRowForKey(key: String): InfoRow? = infoList.firstOrNull { it.key == key }

}
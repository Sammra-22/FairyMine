package com.nordic.fairymine.common.storage

import io.reactivex.Observable


/**
 * Created by Sam22 on 12/30/17.
 */
interface PersistedSettings {
    fun clearSetting(key: PersistedSettingsKey)

    fun <T> storeSetting(key: PersistedSettingsKey, value: T)

    fun <T> readSetting(key: PersistedSettingsKey): T
    fun <T> streamReadSetting(key: PersistedSettingsKey): Observable<T>
}
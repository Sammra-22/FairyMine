@file:Suppress("OVERRIDE_BY_INLINE")

package com.nordic.fairymine.common.storage

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import com.nordic.fairymine.common.storage.model.CachedOffers
import com.nordic.fairymine.common.storage.model.CachedVouchers
import com.nordic.fairymine.common.storage.model.PendingOrders
import io.reactivex.Observable

/**
 * Created by Sam22 on 12/30/17.
 */
class AppSettings(private val sharedPreferences: SharedPreferences) : PersistedSettings {

    private val rxSharedPreferences: RxSharedPreferences = RxSharedPreferences.create(sharedPreferences)

    override fun clearSetting(key: PersistedSettingsKey) {
        sharedPreferences.edit().remove(key.keyName).apply()
    }

    override fun <T> storeSetting(key: PersistedSettingsKey, value: T) {
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key.keyName, value).apply()
            is Float -> editor.putFloat(key.keyName, value).apply()
            is Int -> editor.putInt(key.keyName, value).apply()
            is Long -> editor.putLong(key.keyName, value).apply()
            is Boolean -> editor.putBoolean(key.keyName, value).apply()
            is PendingOrders, is CachedOffers, is CachedVouchers -> editor.putString(key.keyName, Gson().toJson(value)).apply()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> readSetting(key: PersistedSettingsKey): T {
        return when (key.defaultValue) {
            is String -> sharedPreferences.getString(key.keyName, key.defaultValue) as T
            is Float -> sharedPreferences.getFloat(key.keyName, key.defaultValue) as T
            is Int -> sharedPreferences.getInt(key.keyName, key.defaultValue) as T
            is Long -> sharedPreferences.getLong(key.keyName, key.defaultValue) as T
            is Boolean -> sharedPreferences.getBoolean(key.keyName, key.defaultValue) as T
            is PendingOrders -> (sharedPreferences.getString(key.keyName, null)?.let {
                Gson().fromJson(it, PendingOrders::class.java)
            } ?: PendingOrders()) as T
            is CachedOffers -> (sharedPreferences.getString(key.keyName, null)?.let {
                Gson().fromJson(it, CachedOffers::class.java)
            } ?: CachedOffers()) as T
            is CachedVouchers -> (sharedPreferences.getString(key.keyName, null)?.let {
                Gson().fromJson(it, CachedVouchers::class.java)
            } ?: CachedVouchers()) as T
            else -> key.defaultValue as T
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> streamReadSetting(key: PersistedSettingsKey): Observable<T> {
        return when (key.defaultValue) {
            is String -> rxSharedPreferences.getString(key.keyName, key.defaultValue).asObservable() as Observable<T>
            is Float -> rxSharedPreferences.getFloat(key.keyName, key.defaultValue).asObservable() as Observable<T>
            is Int -> rxSharedPreferences.getInteger(key.keyName, key.defaultValue).asObservable() as Observable<T>
            is Long -> rxSharedPreferences.getLong(key.keyName, key.defaultValue).asObservable() as Observable<T>
            is Boolean -> rxSharedPreferences.getBoolean(key.keyName, key.defaultValue).asObservable() as Observable<T>
            is PendingOrders -> rxSharedPreferences.getString(key.keyName, "").asObservable().map {
                if (!it.isEmpty()) Gson().fromJson(it, PendingOrders::class.java) else PendingOrders()
            } as Observable<T>
            is CachedOffers -> rxSharedPreferences.getString(key.keyName, "").asObservable().map {
                if (!it.isEmpty()) Gson().fromJson(it, CachedOffers::class.java) else CachedOffers()
            } as Observable<T>
            is CachedVouchers -> rxSharedPreferences.getString(key.keyName, "").asObservable().map {
                if (!it.isEmpty()) Gson().fromJson(it, CachedVouchers::class.java) else CachedVouchers()
            } as Observable<T>
            else -> Observable.just(key.defaultValue as T)
        }
    }
}
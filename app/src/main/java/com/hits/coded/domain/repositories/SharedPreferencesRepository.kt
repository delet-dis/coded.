package com.hits.coded.domain.repositories

import android.content.SharedPreferences

abstract class SharedPreferencesRepository {
    protected abstract fun getSharedPreferences(): SharedPreferences
    abstract fun <T> updateSharedPreferencesValueByKey(key: String, value: T)
    abstract fun getBooleanValueByKey(key: String): Boolean
}
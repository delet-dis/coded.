package com.hits.coded.data.repositoriesImplementations

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.hits.coded.domain.repositories.SharedPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepositoryImplementation @Inject constructor(@ApplicationContext private val context: Context) :
    SharedPreferencesRepository() {

    override fun getSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    override fun <T> updateSharedPreferencesValueByKey(key: String, value: T) {
        val sharedPreferencesEditor = getSharedPreferences().edit()
        when (value) {
            is Boolean -> {
                sharedPreferencesEditor.putBoolean(key, value).apply()
            }

            is Float -> {
                sharedPreferencesEditor.putFloat(key, value).apply()
            }

            is Int -> {
                sharedPreferencesEditor.putInt(key, value).apply()
            }

            is Long -> {
                sharedPreferencesEditor.putLong(key, value).apply()
            }
        }
    }

    override fun getBooleanValueByKey(key: String): Boolean =
        getSharedPreferences().getBoolean(key, false)

    private companion object {
        const val SETTINGS_NAME = "appSettings"
    }
}
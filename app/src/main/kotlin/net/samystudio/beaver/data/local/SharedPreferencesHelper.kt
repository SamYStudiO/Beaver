package net.samystudio.beaver.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHelper @Inject constructor(
    @param:ApplicationContext private val context: Context, private val sharedPreferences: SharedPreferences
) {
    var apiUrl: String
        get() = sharedPreferences.getString(API_URL, context.getString(R.string.api_url))!!
        set(value) = sharedPreferences.edit { putString(API_URL, value) }

    var accountName: String?
        get() = sharedPreferences.getString(ACCOUNT_NAME, null)
        set(value) = sharedPreferences.edit { putString(ACCOUNT_NAME, value) }

    companion object {
        private const val API_URL = "SharedPreferencesManager:apiUrl"
        private const val ACCOUNT_NAME = "SharedPreferencesManager:accountName"
    }
}
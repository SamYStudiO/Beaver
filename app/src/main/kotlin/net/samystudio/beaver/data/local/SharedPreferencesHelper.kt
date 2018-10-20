package net.samystudio.beaver.data.local

import com.f2prateek.rx.preferences2.RxSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHelper @Inject constructor(rxSharedPreferences: RxSharedPreferences) {
    val apiUrl by lazy { rxSharedPreferences.getString(API_URL) }
    val accountName by lazy { rxSharedPreferences.getString(ACCOUNT_NAME) }
    val accountToken by lazy { rxSharedPreferences.getString(ACCOUNT_TOKEN) }

    companion object {
        private const val API_URL = "SharedPreferencesHelper:apiUrl"
        private const val ACCOUNT_NAME = "SharedPreferencesHelper:accountName"
        private const val ACCOUNT_TOKEN = "SharedPreferencesHelper:accountToken"
    }
}
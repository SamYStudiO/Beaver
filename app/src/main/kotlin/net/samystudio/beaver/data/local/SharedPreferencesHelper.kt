package net.samystudio.beaver.data.local

import com.f2prateek.rx.preferences2.BuildConfig
import com.f2prateek.rx.preferences2.RxSharedPreferences
import net.samystudio.beaver.data.model.Server
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHelper @Inject constructor(
    private val rxSharedPreferences: RxSharedPreferences,
    private val serverList: List<Server>
) {
    val apiUrl by lazy {
        rxSharedPreferences.getString(
            API_URL,
            serverList.find { it.defaultForBuildType == BuildConfig.BUILD_TYPE }?.url
                ?: serverList.firstOrNull()?.url ?: ""
        )
    }
    val accountName by lazy { rxSharedPreferences.getString(ACCOUNT_NAME) }
    val accountToken by lazy { rxSharedPreferences.getString(ACCOUNT_TOKEN) }

    companion object {
        private const val API_URL = "SharedPreferencesHelper:apiUrl"
        private const val ACCOUNT_NAME = "SharedPreferencesHelper:accountName"
        private const val ACCOUNT_TOKEN = "SharedPreferencesHelper:accountToken"
    }
}
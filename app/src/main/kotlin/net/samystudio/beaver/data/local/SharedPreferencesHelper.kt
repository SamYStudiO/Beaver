@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.data.local

import com.f2prateek.rx.preferences2.BuildConfig
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import net.samystudio.beaver.data.model.Server
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHelper @Inject constructor(
    private val servers: Array<Server>,
    private val rxSharedPreferences: RxSharedPreferences,
    private val gson: Gson,
) {
    val token by lazy { rxSharedPreferences.getString(TOKEN) }
    val tokenDate by lazy { rxSharedPreferences.getLong(TOKEN_DATE, 0) }
    val userDate by lazy { rxSharedPreferences.getLong(USER_DATE, 0) }
    val accountId by lazy { rxSharedPreferences.getLong(ACCOUNT_ID) }
    val serverName =
        rxSharedPreferences.getString(
            SERVER_NAME,
            servers.find { it.defaultForBuildType == BuildConfig.BUILD_TYPE }?.name
                ?: servers.firstOrNull()?.name ?: ""
        )
    val server: Server
        get() = servers.find { server ->
            server.name == serverName.get()
        } ?: servers.find { it.defaultForBuildType == BuildConfig.BUILD_TYPE }
            ?: servers.first()

    companion object {
        private const val TOKEN = "SharedPreferencesHelper:token"
        private const val TOKEN_DATE = "SharedPreferencesHelper:tokenDate"
        private const val USER_DATE = "SharedPreferencesHelper:userDate"
        private const val ACCOUNT_ID = "SharedPreferencesHelper:accountId"
        private const val SERVER_NAME = "SharedPreferencesHelper:serverName"
    }
}

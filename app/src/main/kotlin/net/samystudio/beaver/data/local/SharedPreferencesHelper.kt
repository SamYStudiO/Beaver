@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.data.local

import android.util.Base64
import com.f2prateek.rx.preferences2.BuildConfig
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import net.samystudio.beaver.data.model.Server
import net.samystudio.beaver.data.model.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHelper @Inject constructor(
    private val rxSharedPreferences: RxSharedPreferences,
    private val gson: Gson,
    private val serverList: List<Server>,
) {
    val accountName by lazy { rxSharedPreferences.getString(ACCOUNT_NAME) }
    var accountToken
        set(value) {
            if (value != null)
                rxSharedPreferences.getString(ACCOUNT_TOKEN)
                    .set(
                        Base64.encodeToString(
                            (gson.toJson(value) + "1234").toByteArray(),
                            Base64.DEFAULT
                        )
                    )
            else rxSharedPreferences.getString(ACCOUNT_TOKEN).delete()
        }
        get() = if (rxSharedPreferences.getString(ACCOUNT_TOKEN).isSet) gson.fromJson(
            Base64.decode(
                rxSharedPreferences.getString(ACCOUNT_TOKEN).get(),
                Base64.DEFAULT
            ).toString(Charsets.UTF_8).replace("1234", ""),
            Token::class.java
        )
        else null
    val accountTokenObservable =
        rxSharedPreferences.getString(ACCOUNT_TOKEN).asObservable()

    val serverName =
        rxSharedPreferences.getString(
            SERVER_NAME,
            serverList.find { it.defaultForBuildType == BuildConfig.BUILD_TYPE }?.title
                ?: serverList.first().title
        )
    val server
        get() = serverList.find { server ->
            server.title == serverName.get()
        } ?: serverList.find { it.defaultForBuildType == BuildConfig.BUILD_TYPE }
            ?: serverList.first()

    companion object {
        private const val ACCOUNT_NAME = "SharedPreferencesHelper:accountName"
        private const val ACCOUNT_TOKEN = "SharedPreferencesHelper:accountToken"
        private const val SERVER_NAME = "SharedPreferencesHelper:serverName"
    }
}

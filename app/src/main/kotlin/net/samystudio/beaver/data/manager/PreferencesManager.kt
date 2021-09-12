package net.samystudio.beaver.data.manager

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.model.Preferences
import net.samystudio.beaver.data.model.Server
import net.samystudio.beaver.data.model.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    private val servers: List<Server>,
) {
    fun account() = preferencesDataStore.data.map {
        it.account
    }

    fun accountName(onlyIfHasIt: Boolean = false): Flow<String> = preferencesDataStore.data
        .filter { it.account.accountName.isNotBlank() || !onlyIfHasIt }
        .map { it.account.accountName }

    fun accountToken(onlyIfHasIt: Boolean = false) = preferencesDataStore.data
        .filter { it.account.hasAccountToken() || !onlyIfHasIt }
        .map { it.account.accountToken }

    suspend fun updateAccountName(name: String) {
        preferencesDataStore.updateData {
            it.toBuilder().setAccount(
                it.account.toBuilder()
                    .setAccountName(name)
                    .build()
            ).build()
        }
    }

    suspend fun updateAccountToken(token: Token) {
        preferencesDataStore.updateData {
            it.toBuilder().setAccount(
                it.account.toBuilder()
                    .setAccountToken(token)
                    .build()
            ).build()
        }
    }

    suspend fun clearAccountName() {
        preferencesDataStore.updateData {
            it.toBuilder().setAccount(
                it.account.toBuilder()
                    .clearAccountName()
                    .build()
            ).build()
        }
    }

    suspend fun clearAccountToken() {
        preferencesDataStore.updateData {
            it.toBuilder().setAccount(
                it.account.toBuilder()
                    .clearAccountToken()
                    .build()
            ).build()
        }
    }

    /**
     * Get current selected server or first that match [Server.getDefaultForBuildType] from
     * [servers] list if no server has been selected yet.
     */
    fun server() = preferencesDataStore.data.map { preferences ->
        if (preferences.hasServer())
            preferences.server
        else
            servers.find { it.defaultForBuildType == BuildConfig.BUILD_TYPE }
                ?: servers.first()
    }
}

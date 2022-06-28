package net.samystudio.beaver.data.manager

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
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
    private val servers: ArrayList<Server>,
) {
    val accountId: Flow<String> = preferencesDataStore.data
        .map { it.account.accountId }

    val accountToken: Flow<Token> = preferencesDataStore.data
        .map { it.account.accountToken }

    /**
     * Get current selected server or first that match [Server.getDefaultForBuildType] from
     * [servers] list if no server has been selected yet.
     */
    val server: Flow<Server> = preferencesDataStore.data.map { preferences ->
        if (preferences.hasServer())
            preferences.server
        else
            servers.find { it.defaultForBuildType == BuildConfig.BUILD_TYPE }
                ?: servers.first()
    }

    suspend fun updateAccountId(id: String) {
        preferencesDataStore.updateData {
            it.toBuilder().setAccount(
                it.account.toBuilder()
                    .setAccountId(id)
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

    suspend fun clearAccountId() {
        preferencesDataStore.updateData {
            it.toBuilder().setAccount(
                it.account.toBuilder()
                    .clearAccountId()
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
}

@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.*
import net.samystudio.beaver.BuildConfig
import timber.log.Timber
import java.util.*
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Data repository that store a data using 3 layers :
 * - remote (server)
 * - local (e.g. database, SharedPreferences, DataStore, ...), this layer may be optional.
 * - memory (variable [data])
 *
 * Repository allow to refresh data remotely once it's considered not valid anymore either because
 * it is considered out of data or its version has changed.
 */
abstract class DataRepository<T : Any>(
    /**
     * A optional duration to specified how much time local data is considered fresh. Set
     * [Duration.INFINITE] if it should always be considered fresh. If this case the only when to
     * update data is by calling [refresh] manually or if [isVersionValid] is false.
     * [dataRepositoryDateHolder] is required if this is set to a value other than
     * [Duration.INFINITE] otherwise any value set will be ignored.
     */
    val maxAge: Duration = Duration.INFINITE,
    /**
     * A optional [DataRepositoryIntegrityHolder] to store repository data date when it is
     * refreshed. A [maxAge] different than [Duration.INFINITE] is required otherwise any value set
     * will be ignored.
     */
    val dataRepositoryDateHolder: DataRepositoryIntegrityHolder? = null,
    /**
     * A optional [DataRepositoryIntegrityHolder] to store repository data version when it is
     * refreshed. Overriding [getRemoteVersion] is required if this is set to a value in order to
     * compare actual local version and remote version. Leave null if you  want no version check.
     */
    val dataRepositoryVersionHolder: DataRepositoryIntegrityHolder? = null,
) {
    private val _dataFlow = MutableSharedFlow<Optional<T>>()

    /**
     * An [Flow] to listen for any change to [data] no matter if it's null or not.
     */
    val optionalDataFlow: Flow<Optional<T>> = _dataFlow.distinctUntilChanged()

    /**
     * An [Flow] to listen for any change to [data] with a non null value.
     */
    val dataFlow: Flow<T> =
        _dataFlow.distinctUntilChanged().filter { it.isPresent }.map { it.get() }

    private var _data: T? = null

    /**
     * Actual stored data in memory. This may be null if memory data is not set yet, a more secure
     * way to get a valid data is to use [getAsyncData] that will refresh data if it is actually
     * null.
     */
    val data: T?
        get() = _data

    /**
     * Get actual stored data in memory. If null will update data from local or
     * remotely if necessary using [refresh].
     */
    suspend fun getAsyncData(): T = _data ?: refresh()

    /**
     * Get actual local data age.
     */
    suspend fun getAge(): Duration {
        val date = dataRepositoryDateHolder?.getIntegrity() ?: Long.MAX_VALUE
        return abs(System.currentTimeMillis() - date).toDuration(DurationUnit.MILLISECONDS)
    }

    /**
     * Get actual local data version, if [dataRepositoryVersionHolder] is null this has no effect
     * and will return [Duration.ZERO].
     */
    suspend fun getVersion(): Long =
        dataRepositoryVersionHolder?.getIntegrity() ?: 0

    /**
     * Check if actual local data [getAge] is less than [maxAge], if [maxAge] or
     * [dataRepositoryDateHolder] are not set this will always return true.
     */
    open suspend fun isFresh(): Boolean =
        getAge() < maxAge

    /**
     * Check if actual local data version matches [getRemoteVersion] if
     * [dataRepositoryVersionHolder] is not set this will always return true.
     */
    open suspend fun isVersionValid(): Boolean =
        getVersion() == getRemoteVersion()

    /**
     * Check if actual local data [isFresh] and [isVersionValid].
     */
    suspend fun isValid(): Boolean =
        isFresh() && isVersionValid()

    /**
     * Refresh data using the following pattern :
     * - if data is not valid or [checkValidity] is false then get it remotely, it it fails get it
     * locally and if it fails return actual memory data if it's not null. If you want to skip
     * getting data locally when a remote error occurred make sur to call [clear] after a remote
     * error occurred from [getRemoteData].
     * - if data is valid but memory data is not set then get it locally and if it fails
     * get it remotely.
     * - if data is valid and set just get it !
     * @param checkValidity A [Boolean] that indicates if we want to refresh accordingly to
     * [isValid].
     */
    suspend fun refresh(checkValidity: Boolean = true): T =
        when {
            !isValid() || !checkValidity ->
                try {
                    refreshFromRemote()
                } catch (e: Exception) {
                    try {
                        refreshFromLocal()
                    } catch (e: Exception) {
                        _data ?: throw e
                    }
                }
            _data == null ->
                try {
                    refreshFromLocal()
                } catch (e: Exception) {
                    try {
                        refreshFromRemote()
                    } catch (e: Exception) {
                        _data ?: throw e
                    }
                }
            else ->
                _data!!
        }

    /**
     * Refresh data using specified [data] as it was coming remotely.
     */
    suspend fun refresh(data: T) {
        updateData(data, false)
    }

    /**
     * Force refreshing data from remote server no matter current state.
     */
    suspend fun refreshFromRemote(): T =
        getRemoteData().apply { refresh(this) }

    /**
     * Force refreshing data from local no matter current state.
     */
    suspend fun refreshFromLocal(): T {
        val data: T = getLocalData() ?: throw getMissingLocalDataException()
        internalRefresh(data, false)
        return data
    }

    /**
     * Clear current memory and local data, this ensure next call to refresh will get data remotely.
     */
    suspend fun clear() {
        updateData(null, false)
    }

    /**
     * Get actual remote data version. This must be overridden if you want version to be check.
     * To check version [dataRepositoryVersionHolder] must also be set as well.
     */
    protected open fun getRemoteVersion(): Long = 0

    /**
     * Get data remotely.
     */
    protected abstract suspend fun getRemoteData(): T

    /**
     * Optional method to get local data (e.g. database, SharedPreferences, dataStore, ...).
     */
    protected abstract suspend fun getLocalData(): T?

    /**
     * Optional method to store local data (e.g. database, SharedPreferences, dataStore, ...).
     * If null [data] is passed we want to clear data from local data if exists.
     */
    protected abstract suspend fun setLocalData(data: T?)

    protected open fun getMissingLocalDataException(): Exception =
        MissingLocalDataException(this)

    /**
     * Override to do additional work when memory data has changed.
     */
    protected open fun refreshed() {}

    protected suspend fun updateData(data: T?, fromRemote: Boolean) {
        try {
            setLocalData(data)
        } catch (e: Exception) {
            // If we fail writing locally don't throw an error, we'll try next time.
        }
        internalRefresh(data, fromRemote)
    }

    /**
     * Update data date and version using [System.currentTimeMillis] and [BuildConfig.VERSION_CODE]
     * by default. Override this to set a custom value for each of them.
     */
    protected open suspend fun updateDateAndVersion() {
        dataRepositoryDateHolder?.setIntegrity(System.currentTimeMillis())
        dataRepositoryVersionHolder?.setIntegrity(BuildConfig.VERSION_CODE.toLong())
    }

    protected suspend fun clearDateAndVersion() {
        dataRepositoryDateHolder?.setIntegrity(0)
        dataRepositoryVersionHolder?.setIntegrity(0)
    }

    private suspend fun internalRefresh(data: T?, fromRemote: Boolean) {
        if (_data == data && _dataFlow.replayCache.isNotEmpty()) return

        Timber.d("internalRefresh: $this from server = $fromRemote")

        _data = data
        if (data == null)
            clearDateAndVersion()
        else
            updateDateAndVersion()

        refreshed()
        _dataFlow.emit(Optional.ofNullable(data))
    }
}

@Suppress("CanBeParameter")
class MissingLocalDataException(val repository: DataRepository<*>) :
    Exception(repository.toString())

/**
 * A holder to store repository date or version as a [Long].
 * 0 value should be considered as not set.
 */
interface DataRepositoryIntegrityHolder {
    suspend fun getIntegrity(): Long
    suspend fun setIntegrity(integrity: Long)
}

/**
 * A [DataRepositoryIntegrityHolder] implementation using [SharedPreferences].
 */
class SharedPreferencesDataRepositoryIntegrityHolder(
    private val sharedPreferences: SharedPreferences
) : DataRepositoryIntegrityHolder {
    private val id = UUID.randomUUID().toString()

    override suspend fun getIntegrity(): Long =
        sharedPreferences.getLong(id, 0L)

    override suspend fun setIntegrity(integrity: Long) {
        if (integrity == 0L)
            sharedPreferences.edit().remove(id).apply()
        else
            sharedPreferences.edit().putLong(id, integrity).apply()
    }
}
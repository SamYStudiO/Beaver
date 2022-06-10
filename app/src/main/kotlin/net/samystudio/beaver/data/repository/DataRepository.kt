@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.*
import kotlin.time.Duration

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
     * A duration to specified how much time local data is considered fresh. Set [Duration.INFINITE]
     * if it should always be considered fresh. If this case the only when to update data is by
     * calling [refresh] manually or if [isVersionValid] is false.
     */
    val maxAge: Duration,
) {
    /**
     * [Observable] to get data remotely with fallback to local data. This is a shared observable
     * so this may never be called several time at the same time.
     */
    private val remoteDataObservable = Single.defer {
        getRemoteData(getRemoteDataSingle())
            .onErrorResumeWith(Single.defer { getLocalData() })
            .onErrorResumeNext { throwable ->
                _data?.let { Single.just(it) } ?: Single.error(throwable)
            }
    }.toObservable().share()
    /**
     * [Observable] to get data locally with fallback to remote data. This is a shared observable
     * so this may never be called several time at the same time.
     */
    private val localDataObservable = Single.defer {
        getLocalData()
            .onErrorResumeWith(Single.defer { getRemoteData(getRemoteDataSingle()) })
    }.toObservable().share()
    private val _dataObservable = BehaviorSubject.create<Optional<T>>()

    /**
     * An [Observable] to listen for any change to [data] no matter if it's null or not.
     */
    val optionalDataObservable: Observable<Optional<T>> = _dataObservable.distinctUntilChanged()

    /**
     * An [Observable] to listen for any change to [data] with a non null value.
     */
    val dataObservable: Observable<T> =
        _dataObservable.distinctUntilChanged().filter { it.isPresent }.map { it.get() }

    private var _data: T? = null

    /**
     * Actual stored data in memory. This may be null if memory data is not set yet, a more secure
     * way to get a valid data is to use [asyncData] that will refresh data if it is actually null.
     */
    val data: T?
        get() = _data

    /**
     * Get a [Single] of actual stored data in memory. If null will update data from local or
     * remotely if necessary using [refresh].
     */
    val asyncData: Single<T>
        get() = _data?.let { Single.just(it) } ?: refresh()

    /**
     * Check if actual local data [getAge] is less than [maxAge].
     */
    open val isFresh
        get() = getAge() < maxAge

    /**
     * Check if actual local data version matches [getLocalVersion].
     */
    open val isVersionValid
        get() = getLocalVersion() == getRemoteVersion()

    /**
     * Check if actual local data [isFresh] and [isVersionValid].
     */
    val isValid
        get() = isFresh && isVersionValid

    /**
     * Refresh data using the following pattern :
     * - if data is not valid or [checkValidity] is false then get it remotely, it it fails get it
     * locally and if it fails return actual memory data if it's not null. If you want to skip
     * getting data locally when a remote error occurred make sur to call [clear] after a remote
     * error occurred from [getRemoteDataSingle].
     * - if data is valid but memory data is not set then get it locally and if it fails
     * get it remotely.
     * - if data is valid and set just get it !
     * @param checkValidity A [Boolean] that indicates if we want to refresh accordingly to
     * [isValid].
     */
    fun refresh(checkValidity: Boolean = true): Single<T> =
        when {
            !isValid || !checkValidity ->
                remoteDataObservable.lastOrError()
            _data == null ->
                localDataObservable.lastOrError()
            else ->
                Single.defer { Single.just(_data!!) }
        }

    /**
     * Refresh data using specified [data] as it was coming remotely.
     */
    fun refresh(data: T): Single<T> = Single.defer {
        getRemoteData(Single.just(data))
    }

    /**
     * Force refreshing data from remote server no matter current state.
     */
    fun refreshFromRemote(): Single<T> = Single.defer {
        getRemoteData(getRemoteDataSingle())
    }

    /**
     * Clear current memory and local data, this ensure next call to refresh will get data remotely.
     */
    fun clear(): Completable = Completable.defer {
        (setLocalDataSingle(null) ?: Completable.complete()).doOnTerminate {
            internalRefresh(null, false)
        }
    }

    /**
     * Get actual local data age, if [maxAge] is [Duration.INFINITE] this has no effect.
     */
    protected abstract fun getAge(): Duration

    /**
     * Get actual local data version. By default there is no data versioning, this means
     * [getLocalVersion] and [getRemoteVersion] always return an empty [String] and [isVersionValid]
     * is always true.
     */
    protected open fun getLocalVersion(): String = ""

    /**
     * Get actual remote data version. By default there is no data versioning, this means
     * [getLocalVersion] and [getRemoteVersion] always return an empty [String] and [isVersionValid]
     * is always true.
     */
    protected open fun getRemoteVersion(): String = ""

    /**
     * [Single] to get data remotely.
     */
    protected abstract fun getRemoteDataSingle(): Single<T>

    /**
     * Optional [single] to get local data (e.g. database, SharedPreferences, dataStore, ...).
     */
    protected abstract fun getLocalDataSingle(): Single<T>?

    /**
     * Optional [Completable] to store local data (e.g. database, SharedPreferences, dataStore, ...).
     * If null [data] is passed we want to clear data from local data if exists.
     * It is the good time and you own responsibility to store data timing and version that will
     * be then retrieve from [getAge] and [getLocalVersion], when [data] passed is null this timing
     * and version should be reset.
     */
    protected abstract fun setLocalDataSingle(data: T?): Completable?

    protected open fun getMissingLocalDataException(): Exception = MissingLocalDataException(this)

    /**
     * Override to do additional work when memory data has changed.
     */
    protected open fun refreshed() {}

    protected fun getRemoteData(remoteSingle: Single<T>): Single<T> =
        remoteSingle.flatMap {
            // If we fail writing locally don't throw an error, we'll try next time.
            setLocalDataSingle(it)?.onErrorComplete()?.andThen(Single.just(it)) ?: Single.just(it)
        }.doOnSuccess { internalRefresh(it, true) }
            .doOnError {
                Timber.d("error getting data remotely from $this ${it.message}")
            }

    protected fun getLocalData(): Single<T> =
        getLocalDataSingle()
            ?.doOnSuccess { internalRefresh(it, false) }
            ?.doOnError {
                Timber.d("error getting data locally from $this ${it.message}")
            }
            ?.onErrorResumeNext { Single.error(getMissingLocalDataException()) }
            ?: Single.error(getMissingLocalDataException())

    private fun internalRefresh(data: T?, fromRemote: Boolean) {
        if (_data == data && _dataObservable.value != null) return

        Timber.d("internalRefresh: $this from server = $fromRemote")

        _data = data
        refreshed()
        _dataObservable.onNext(Optional.ofNullable(data))
    }
}

class MissingLocalDataException(val repository: DataRepository<*>) :
    Exception(repository.toString())

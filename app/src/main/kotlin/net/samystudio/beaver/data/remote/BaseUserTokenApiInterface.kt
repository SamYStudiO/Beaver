@file:Suppress("unused")

package net.samystudio.beaver.data.remote

import dagger.Lazy
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.functions.Function
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.remote.retrofit.RetrofitException
import org.reactivestreams.Publisher
import retrofit2.HttpException
import java.net.HttpURLConnection

/**
 * Helper to compose all api request that need a user token to make sure a automatic sign in is done
 * when user token is expired.
 */
abstract class BaseUserTokenApiInterface(protected val userManager: Lazy<UserManager>) {
    fun <T> onTokenInvalidSingleTransformer(): SingleTransformer<T, T> =
        SingleTransformer { upstream ->
            upstream.retryWhen(onTokenInvalidTransformer())
        }

    fun onTokenInvalidCompletableTransformer(): CompletableTransformer =
        CompletableTransformer { upstream ->
            upstream.retryWhen(onTokenInvalidTransformer())
        }

    private fun onTokenInvalidTransformer() =
        Function<Flowable<Throwable>, Publisher<*>> {
            it.flatMap { throwable ->
                if (isThrowableUnauthorized(throwable))
                    userManager.get()
                        .refreshToken()
                        .onErrorComplete() // If an error occurred swallow it as disconnecting is already handled from userManager.
                        .toFlowable()
                else {
                    Flowable.error<Throwable>(throwable)
                }
            }
        }

    private fun isThrowableUnauthorized(throwable: Throwable) =
        (throwable is HttpException && throwable.code() == HttpURLConnection.HTTP_UNAUTHORIZED) ||
            (throwable is RetrofitException && throwable.code == HttpURLConnection.HTTP_UNAUTHORIZED)
}

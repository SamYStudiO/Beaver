package net.samystudio.beaver.data.remote

import dagger.Lazy
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.functions.Function
import net.samystudio.beaver.data.manager.UserManager
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
                (if (throwable is HttpException && throwable.code() == HttpURLConnection.HTTP_UNAUTHORIZED && userManager.get().isConnected)
                    userManager.get().refreshToken().toFlowable()
                else {
                    Flowable.error<Throwable>(throwable)
                }).doOnError { userManager.get().disconnect() }
            }
        }
}

package net.samystudio.beaver.data.remote.retrofit

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.remote.retrofit.RetrofitException.Companion.asRetrofitException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CallAdapter for [Retrofit] to convert all [HttpException] to [RetrofitException].
 */
@Singleton
class RxErrorHandlingCallAdapterFactory
@Inject constructor(private val original: RxJava3CallAdapterFactory) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(
            original.get(returnType, annotations, retrofit) ?: return null,
            retrofit
        )
    }

    private class RxCallAdapterWrapper<R>(
        private val wrapped: CallAdapter<R, *>,
        private val retrofit: Retrofit
    ) : CallAdapter<R, Any> {
        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun adapt(call: Call<R>): Any {
            return when (val result = wrapped.adapt(call)) {
                is Single<*> -> result.onErrorResumeNext { throwable ->
                    Single.error(asRetrofitException(retrofit, throwable))
                }
                is Observable<*> -> result.onErrorResumeNext { throwable: Throwable ->
                    Observable.error(asRetrofitException(retrofit, throwable))
                }
                is Completable -> result.onErrorResumeNext { throwable ->
                    Completable.error(asRetrofitException(retrofit, throwable))
                }
                else -> result
            }
        }
    }
}

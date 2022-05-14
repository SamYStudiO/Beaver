package net.samystudio.beaver.data.remote.retrofit

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxJava3WithLogCallAdapterFactory
@Inject constructor(
    private val original: RxJava3CallAdapterFactory,
    private val crashlytics: FirebaseCrashlytics
) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(
            original.get(returnType, annotations, retrofit) ?: return null,
            crashlytics
        )
    }

    private class RxCallAdapterWrapper<R>(
        private val wrapped: CallAdapter<R, *>,
        private val crashlytics: FirebaseCrashlytics
    ) : CallAdapter<R, Any> {
        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun adapt(call: Call<R>): Any {
            return when (val result = wrapped.adapt(call)) {
                is Single<*> -> result.doOnError {
                    if (it !is HttpException) return@doOnError
                    crashlytics.log("RetrofitException url=${call.request().url}, code=${it.code()}, message=${it.message()}")
                    crashlytics.recordException(it)
                }
                is Observable<*> -> result.doOnError {
                    if (it !is HttpException) return@doOnError
                    crashlytics.log("RetrofitException url=${call.request().url}, code=${it.code()}, message=${it.message()}")
                    crashlytics.recordException(it)
                }
                is Completable -> result.doOnError {
                    if (it !is HttpException) return@doOnError
                    crashlytics.log("RetrofitException url=${call.request().url}, code=${it.code()}, message=${it.message()}")
                    crashlytics.recordException(it)
                }
                else -> result
            }
        }
    }
}

@file:Suppress("unused")

package net.samystudio.beaver.data.remote.retrofit

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

/**
 * Wrapper for [HttpException] that convert an error body json to an [ApiError] object, this is
 * useful when server provides a custom object when an error occurred, Adjust [ApiError] to match
 * server data.
 */
class RetrofitException private constructor(
    message: String?,
    exception: Throwable,
    /**
     * The server response code or 0.
     */
    val code: Int = 0,
    /**
     * The request URL which produced the error.
     */
    val url: String?,
    /**
     * Response object containing status code, headers, body, etc.
     */
    val errorBody: ApiError?,
    /**
     * The event kind which triggered this error.
     */
    val kind: Kind

) : RuntimeException(message, exception) {
    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Kind {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,

        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    companion object {
        fun asRetrofitException(retrofit: Retrofit, throwable: Throwable): RetrofitException {
            if (throwable is RetrofitException)
                return throwable

            // We had non-200 http error.
            if (throwable is HttpException) {
                val response = throwable.response()
                val converter: Converter<ResponseBody, ApiError> =
                    retrofit.responseBodyConverter(
                        ApiError::class.java,
                        arrayOfNulls<Annotation>(0)
                    )
                val error: ApiError? = response?.errorBody()?.let { converter.convert(it) }
                return httpError(
                    throwable,
                    response?.raw()?.request?.url?.toString(),
                    error
                )
            }

            // A network error happened.
            return if (throwable is IOException) {
                networkError(throwable)
            } else unexpectedError(throwable)
            // We don't know what happened. We need to simply convert to an unknown error.
        }

        private fun httpError(
            httpException: HttpException,
            url: String?,
            errorBody: ApiError?
        ): RetrofitException {
            val message =
                httpException.response()?.code()?.toString() + " " + httpException.response()
                    ?.message()
            return RetrofitException(
                message,
                httpException,
                httpException.code(),
                url,
                errorBody,
                Kind.HTTP
            )
        }

        private fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(
                exception.message,
                exception,
                0,
                null,
                null,
                Kind.NETWORK
            )
        }

        private fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(
                exception.message,
                exception,
                0,
                null,
                null,
                Kind.UNEXPECTED
            )
        }
    }
}

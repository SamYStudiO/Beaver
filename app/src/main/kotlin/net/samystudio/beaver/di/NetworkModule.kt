package net.samystudio.beaver.di

import android.app.Application
import androidx.core.os.LocaleListCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.schedulers.Schedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.model.isValid
import net.samystudio.beaver.data.remote.isAuthorizationRequired
import net.samystudio.beaver.data.remote.retrofit.RxJava3WithLogCallAdapterFactory
import net.samystudio.beaver.data.repository.TokenRepository
import net.samystudio.beaver.data.repository.UserRepository
import net.samystudio.beaver.util.NETWORK_CACHE_SIZE
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL: String = "https://root/"

    @Provides
    @Singleton
    fun provideCache(application: Application): Cache =
        Cache(application.cacheDir, NETWORK_CACHE_SIZE)

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        tokenRepository: Lazy<TokenRepository>,
        userRepository: Lazy<UserRepository>,
    ): Authenticator = Authenticator { _, response ->
        val request = response.request

        // If this request doesn't require authentication, this certainly means we try to refresh
        // token unsuccessfully, so we can stop here otherwise we may enter a loop.
        if (!request.isAuthorizationRequired())
            return@Authenticator null

        // Try to get actual token.
        val actualToken = tokenRepository.get().data.takeIf { it.isValid } ?: run {
            userRepository.get().clear().onErrorComplete().blockingAwait()
            return@Authenticator null
        }

        // Use synchronized in case we have multiple request ending up with 401 then we don't want
        // to refresh token multiple times.
        return@Authenticator synchronized(this) {
            // We need to check now if we haven't yet refresh a new token, in this case
            // the new refresh token should be different than actualToken.
            val newToken =
                if (actualToken == tokenRepository.get().data)
                    try {
                        // This will write a new token to sharedPreferences.
                        tokenRepository.get().refreshFromRemote().blockingGet()
                    } catch (e: Exception) {
                        // A error occurred while refreshing token.
                        Timber.w(e)
                        null
                    }
                else // Should be already refreshed.
                    tokenRepository.get().data

            // If we have a new token let's use it.
            newToken?.takeIf { it.isValid }?.let {
                request.newBuilder()
                    .header("Authorization", "${it.tokenType} ${it.accessToken}")
                    .build()
            }
        }
    }

    @Provides
    @Singleton
    fun provideRequestInterceptor(
        tokenRepository: Lazy<TokenRepository>,
        sharedPreferencesHelper: SharedPreferencesHelper,
    ): Interceptor =
        Interceptor { chain ->
            // Request interceptor to update root url and add user token if exist.
            val request = chain.request()
            val url = request.url

            // Rewriting url is not necessary when using a unique production server url, but in most
            // case we'll use multiple server urls  (test/prod/...) and this is the way to go if we
            // want to update Retrofit base url at runtime.
            val newBuilder = request.newBuilder()

            // Let's set selected server url.
            newBuilder
                .url(
                    url.toString().replace(BASE_URL, sharedPreferencesHelper.server.url).toHttpUrl()
                )
                .header(
                    "Accept-Language",
                    LocaleListCompat.getAdjustedDefault().let { localeList ->
                        mutableListOf<Locale>().apply {
                            repeat(localeList.size()) { index ->
                                localeList.get(index)?.let { add(it) }
                            }
                        }.joinToString(", ") { it.toString() }
                    }
                )

            // Let's add token if we got one and if required.
            if (request.isAuthorizationRequired()) {
                try {
                    tokenRepository.get().asyncData.blockingGet().takeIf { it.isValid }?.let {
                        val type = it.tokenType
                        val accessToken = it.accessToken
                        newBuilder.header("Authorization", "$type $accessToken")
                    }
                } catch (e: Exception) {
                }
            }

            chain.proceed(newBuilder.build())
        }

    @Provides
    @Singleton
    @OkHttpRetrofitQualifier
    fun provideOkHttpClient(
        cache: Cache,
        authenticator: Authenticator,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .authenticator(authenticator)
            .addInterceptor(requestInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideNullOrEmptyConverterFactory(): Converter.Factory =
        object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
                    this,
                    type,
                    annotations
                )

                return Converter { body: ResponseBody ->
                    if (body.contentLength() == 0L) null
                    else nextResponseBodyConverter.convert(body)
                }
            }
        }

    @Provides
    @Singleton
    fun provideRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory =
        RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    @Singleton
    fun provideRxJava3WithLogCallAdapterFactory(
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
        crashlytics: FirebaseCrashlytics
    ): RxJava3WithLogCallAdapterFactory =
        RxJava3WithLogCallAdapterFactory(rxJava3CallAdapterFactory, crashlytics)

    @Provides
    @Singleton
    fun provideRetrofit(
        @OkHttpRetrofitQualifier okHttpClient: OkHttpClient,
        rxJava3WithLogCallAdapterFactory: RxJava3WithLogCallAdapterFactory,
        nullOrEmptyConverterFactory: Converter.Factory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava3WithLogCallAdapterFactory)
            .addConverterFactory(nullOrEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(BASE_URL)
            .build()
}

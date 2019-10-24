package net.samystudio.beaver.di.module

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.remote.api.AuthenticatorApiInterface
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
object NetworkModule {
    private const val BASE_URL: String = "https://root/"

    @Provides
    @Singleton
    @JvmStatic
    fun provideCache(application: Application): Cache =
        Cache(application.cacheDir, 20L * 1024 * 1024) //20 mo

    @Provides
    @Singleton
    @JvmStatic
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideOkHttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(requestInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    @JvmStatic
    fun provideRequestInterceptor(
        sharedPreferencesHelper: SharedPreferencesHelper,
        userManager: UserManager
    ): Interceptor =
        Interceptor { chain ->
            // Request interceptor to update root url and add user token if exist.
            val request = chain.request()
            val url = request.url.toString()

            // rewriting url is not necessary when using a unique production server url, but in most
            // case we'll use multiple server urls  (test/prod/...) and this is the way to go if we
            // want to update Retrofit base url at runtime
            val newBuilder = request.newBuilder()

            url.replace(BASE_URL, sharedPreferencesHelper.apiUrl.get()).toHttpUrlOrNull()?.let {
                newBuilder.url(it)
            }

            // let's add token if we got one
            userManager.token?.let { newBuilder.header("Authorization", "Bearer $it") }

            chain.proceed(newBuilder.build())
        }

    @Provides
    @Singleton
    @JvmStatic
    fun provideGson(): Gson =
        GsonBuilder()
            .create()

    @Provides
    @Singleton
    @JvmStatic
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    @JvmStatic
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
    @JvmStatic
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory =
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        nullOrEmptyConverterFactory: Converter.Factory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(nullOrEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(BASE_URL)
            .build()

    @Provides
    @Singleton
    @JvmStatic
    fun provideAuthenticatorApiInterface(retrofit: Retrofit): AuthenticatorApiInterface =
        retrofit.create(AuthenticatorApiInterface::class.java)
}
package net.samystudio.beaver.di.module

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.remote.api.AuthenticatorApiInterface
import net.samystudio.beaver.di.qualifier.ApplicationContext
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
object NetworkModule
{
    private const val BASE_URL: String = "https://root/"

    @Provides
    @Singleton
    @JvmStatic
    fun provideCache(application: Application) =
        Cache(application.cacheDir, 20L * 1024 * 1024) //20 mo

    @Provides
    @Singleton
    @JvmStatic
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor
    {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE

        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    @ApplicationContext
    @JvmStatic
    fun provideOkHttpClient(cache: Cache,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            requestInterceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(requestInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    @JvmStatic
    fun provideRequestInterceptor(sharedPreferencesHelper: SharedPreferencesHelper,
                                  userManager: UserManager): Interceptor =
        Interceptor { chain ->
            // Request interceptor to update root url and add user token if exist.

            val request = chain.request()
            val url = request.url().url().toString()

            // rewriting url is not necessary when using a unique production server url, but in most
            // case we'll use multiple server urls  (test/prod/...) and this is the way to go if we
            // want to update Retrofit base url at runtime
            val httpUrl = HttpUrl.parse(url.replace(BASE_URL, sharedPreferencesHelper.apiUrl))
            val newBuilder = request.newBuilder()

            if (httpUrl != null)
                newBuilder.url(httpUrl)

            // let's add token if we got one
            userManager.token?.let { newBuilder.header("Authorization", it) }

            chain.proceed(newBuilder.build())
        }

    @Provides
    @Singleton
    @JvmStatic
    fun provideGson(): Gson =
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
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
        object : Converter.Factory()
        {
            override fun responseBodyConverter(type: Type,
                                               annotations: Array<out Annotation>,
                                               retrofit: Retrofit): Converter<ResponseBody, Any?>
            {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
                    this,
                    type,
                    annotations)

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
    fun provideRetrofit(rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                        nullOrEmptyConverterFactory: Converter.Factory,
                        @ApplicationContext okHttpClient: OkHttpClient,
                        gsonConverterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(nullOrEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @JvmStatic
    fun provideAuthenticatorApiInterface(retrofit: Retrofit): AuthenticatorApiInterface =
        retrofit.create(AuthenticatorApiInterface::class.java)
}

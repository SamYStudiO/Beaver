package net.samystudio.beaver.di.module

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.manager.SharedPreferencesManager
import net.samystudio.beaver.di.qualifier.ApplicationContext
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
class NetModule
{
    @Provides
    @Singleton
    fun provideCache(application: Application) =
            Cache(application.cacheDir, 20L * 1024L * 1024L) //20 mo

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache,
                            sharedPreferencesManager: SharedPreferencesManager): OkHttpClient
    {
        val builder = OkHttpClient.Builder().cache(cache)

        // Request interceptor to update root url and add user token if exist.
        builder.addInterceptor({ chain ->
                                   val request = chain.request()
                                   val url = request.url().url().toString()

                                   // rewriting url is not necessary when using a unique production
                                   // server url, but in most case we'll use multiple server urls
                                   // (test/prod/...) and this is the way to go if we want to update
                                   // Retrofit base url at runtime
                                   val httpUrl = HttpUrl.parse(url.replace(BASE_URL,
                                                                           sharedPreferencesManager.apiUrl))

                                   val newBuilder = request.newBuilder()

                                   if (httpUrl != null)
                                       newBuilder.url(httpUrl)

                                   val token = sharedPreferencesManager.userToken

                                   if (token != null)
                                       newBuilder.header("Authorization", token)

                                   chain.proceed(newBuilder.build())
                               })

        if (BuildConfig.DEBUG)
        {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson =
            GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .build()

    @Provides
    @Singleton
    fun providePicasso(@ApplicationContext context: Context, okHttpClient: OkHttpClient): Picasso =
            Picasso.Builder(context)
                    .downloader(OkHttp3Downloader(okHttpClient))
                    .indicatorsEnabled(BuildConfig.DEBUG)
                    .loggingEnabled(BuildConfig.DEBUG)
                    .listener { _, _, exception -> Timber.w(exception) }
                    .build()

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context: Context, okHttpClient: OkHttpClient): Glide
    {

        AppGlideModule
        Glide.get(context).registry.register(GlideUrl::class)
        GlideBuilder().build(context)

        Glide.Builder(context)
                .downloader(OkHttp3Downloader(okHttpClient))
                .indicatorsEnabled(BuildConfig.DEBUG)
                .loggingEnabled(BuildConfig.DEBUG)
                .listener { _, _, exception -> Timber.w(exception) }
                .build()
    }

    private class NullOnEmptyConverterFactory : Converter.Factory()
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

    companion object
    {
        const val BASE_URL: String = "https://root/"
    }
}

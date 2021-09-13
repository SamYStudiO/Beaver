package net.samystudio.beaver.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.samystudio.beaver.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context).apply {
            setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        }

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics =
        FirebaseCrashlytics.getInstance().apply {
            setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        }
}

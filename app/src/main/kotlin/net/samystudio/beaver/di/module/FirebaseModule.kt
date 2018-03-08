package net.samystudio.beaver.di.module

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.ApplicationContext
import javax.inject.Singleton

/**
 * https://firebase.google.com/docs/crashlytics/
 */
@Module
object FirebaseModule
{
    @Provides
    @Singleton
    @JvmStatic
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
}

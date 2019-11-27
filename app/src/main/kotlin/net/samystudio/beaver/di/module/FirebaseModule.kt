package net.samystudio.beaver.di.module

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.ApplicationContext
import javax.inject.Singleton

@Module
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
}
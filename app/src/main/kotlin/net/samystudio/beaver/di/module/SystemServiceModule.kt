package net.samystudio.beaver.di.module

import android.accounts.AccountManager
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.ApplicationContext
import javax.inject.Singleton

/**
 * All kind of data provided by Android system.
 */
@Module
object SystemServiceModule {
    @Provides
    @Singleton
    @JvmStatic
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    @JvmStatic
    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences =
        RxSharedPreferences.create(sharedPreferences)

    @Provides
    @Singleton
    @JvmStatic
    fun provideAccountManager(@ApplicationContext context: Context): AccountManager =
        AccountManager.get(context)
}
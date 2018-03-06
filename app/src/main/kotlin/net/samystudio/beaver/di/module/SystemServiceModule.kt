package net.samystudio.beaver.di.module

import android.accounts.AccountManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.ApplicationContext
import javax.inject.Singleton

/**
 * All kind of data provided by Android system.
 * Mostly singleton retrieved using [android.content.Context.getSystemService].
 */
@Module
object SystemServiceModule
{
    @Provides
    @Singleton
    @JvmStatic
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    @JvmStatic
    fun provideAccountManager(@ApplicationContext context: Context): AccountManager =
        AccountManager.get(context)
}

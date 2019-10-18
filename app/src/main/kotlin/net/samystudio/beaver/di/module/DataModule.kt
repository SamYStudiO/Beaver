package net.samystudio.beaver.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.data.local.BeaverDatabase
import net.samystudio.beaver.di.qualifier.ApplicationContext
import javax.inject.Singleton

@Module
object DataModule {
    private const val DATABASE_NAME: String = "database-beaver"

    @Provides
    @Singleton
    @JvmStatic
    fun provideDatabase(@ApplicationContext context: Context): RoomDatabase =
        Room.databaseBuilder(context, BeaverDatabase::class.java, DATABASE_NAME).build()
}
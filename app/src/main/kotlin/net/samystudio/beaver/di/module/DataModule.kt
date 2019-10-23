package net.samystudio.beaver.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.R
import net.samystudio.beaver.data.local.BeaverDatabase
import net.samystudio.beaver.data.model.Server
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

    @Provides
    @Singleton
    @JvmStatic
    fun provideServerList(@ApplicationContext context: Context, gson: Gson): List<Server> =
        gson.fromJson(
            context.resources.openRawResource(R.raw.servers).bufferedReader(),
            TypeToken.getParameterized(List::class.java, Server::class.java).type
        )
}
package net.samystudio.beaver.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.samystudio.beaver.R
import net.samystudio.beaver.data.local.BeaverDatabase
import net.samystudio.beaver.data.model.Server
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val DATABASE_NAME: String = "database-beaver"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RoomDatabase =
        Room.databaseBuilder(context, BeaverDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideServerList(@ApplicationContext context: Context, gson: Gson): List<Server> =
        gson.fromJson(
            context.resources.openRawResource(R.raw.servers).bufferedReader(),
            TypeToken.getParameterized(List::class.java, Server::class.java).type
        )
}
package net.samystudio.beaver.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.R
import net.samystudio.beaver.data.local.BeaverDatabase
import net.samystudio.beaver.data.local.UserDao
import net.samystudio.beaver.data.model.Server
import net.samystudio.beaver.data.remote.AuthenticatorApiInterface
import net.samystudio.beaver.data.remote.UserApiInterface
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationDataModule {
    private const val DATABASE_NAME: String = "database-beaver"

    @Provides
    @Singleton
    fun provideServerList(@ApplicationContext context: Context, gson: Gson): Array<Server> =
        gson.fromJson<List<Server>>(
            context.resources.openRawResource(R.raw.servers).bufferedReader(),
            TypeToken.getParameterized(List::class.java, Server::class.java).type
        ).toTypedArray()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BeaverDatabase =
        Room.databaseBuilder(context, BeaverDatabase::class.java, DATABASE_NAME).apply {
            if (BuildConfig.DEBUG)
                fallbackToDestructiveMigration()
        }.build()

    @Provides
    @Singleton
    fun provideUserDao(database: BeaverDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideAuthenticatorApiInterface(retrofit: Retrofit): AuthenticatorApiInterface =
        retrofit.create(AuthenticatorApiInterface::class.java)

    @Provides
    @Singleton
    fun provideProfileApiInterface(retrofit: Retrofit): UserApiInterface =
        retrofit.create(UserApiInterface::class.java)
}

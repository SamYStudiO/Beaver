package net.samystudio.beaver.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.protobuf.InvalidProtocolBufferException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.R
import net.samystudio.beaver.data.local.BeaverDatabase
import net.samystudio.beaver.data.local.UserDao
import net.samystudio.beaver.data.model.Preferences
import net.samystudio.beaver.data.model.Server
import net.samystudio.beaver.data.remote.AuthenticatorApiInterface
import net.samystudio.beaver.data.remote.UserApiInterface
import retrofit2.Retrofit
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationDataModule {
    private const val PREFERENCES_FILE_NAME: String = "preferences.proto"
    private const val DATABASE_FILE_NAME: String = "database-beaver"
    private val Context.preferencesDataStore: DataStore<Preferences> by dataStore(
        fileName = PREFERENCES_FILE_NAME,
        serializer = PreferencesSerialize()
    )

    @Provides
    @Singleton
    fun provideServerList(@ApplicationContext context: Context, gson: Gson): ArrayList<Server> =
        gson.fromJson(
            context.resources.openRawResource(R.raw.servers).bufferedReader(),
            TypeToken.getParameterized(ArrayList::class.java, Server::class.java).type
        )

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.preferencesDataStore

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BeaverDatabase =
        Room.databaseBuilder(context, BeaverDatabase::class.java, DATABASE_FILE_NAME).apply {
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

@Suppress("BlockingMethodInNonBlockingContext")
private class PreferencesSerialize : Serializer<Preferences> {
    override val defaultValue: Preferences = Preferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Preferences {
        try {
            return Preferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) = t.writeTo(output)
}

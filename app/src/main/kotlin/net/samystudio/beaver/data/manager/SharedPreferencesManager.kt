package net.samystudio.beaver.data.manager

import android.content.Context
import android.content.SharedPreferences
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.ApplicationContext

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(@param:ApplicationContext
                                                   private val context: Context,
                                                   private val sharedPreferences: SharedPreferences)
{
    var apiUrl: String
        get() = sharedPreferences.getString(API_URL, context.getString(R.string.api_url))
        set(value) = sharedPreferences.edit().putString(API_URL, value).apply()

    var userToken: String?
        get() = sharedPreferences.getString(USER_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(USER_TOKEN, value).apply()

    companion object
    {
        const val API_URL = "apiUrl"
        const val USER_TOKEN = "userToken"
    }
}

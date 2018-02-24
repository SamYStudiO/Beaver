@file:Suppress("unused")

package net.samystudio.beaver.ext

import android.content.SharedPreferences

fun SharedPreferences.putString(key: String, value: String?) = edit().putString(key, value).apply()
fun SharedPreferences.putInt(key: String, value: Int) = edit().putInt(key, value).apply()
fun SharedPreferences.putLong(key: String, value: Long) = edit().putLong(key, value).apply()
fun SharedPreferences.putFloat(key: String, value: Float) = edit().putFloat(key, value).apply()
fun SharedPreferences.putBoolean(key: String, value: Boolean) = edit().putBoolean(key,
                                                                                  value).apply()
fun SharedPreferences.remove(key: String) = edit().remove(key).apply()
fun SharedPreferences.clear() = edit().clear().apply()

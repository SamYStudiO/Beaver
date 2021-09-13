@file:Suppress("unused")

package net.samystudio.beaver.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import net.samystudio.beaver.ContextProvider.Companion.applicationContext

fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    applicationContext, permission
) == PackageManager.PERMISSION_GRANTED

fun Activity.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    requireContext(),
    permission
) == PackageManager.PERMISSION_GRANTED

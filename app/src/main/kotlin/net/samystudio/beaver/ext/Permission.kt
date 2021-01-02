package net.samystudio.beaver.ext

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Activity.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    requireContext(),
    permission
) == PackageManager.PERMISSION_GRANTED

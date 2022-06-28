@file:Suppress("unused")

package net.samystudio.beaver.util

import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import net.samystudio.beaver.R
import timber.log.Timber

fun AppCompatActivity.popBackStack(@IdRes hostId: Int) {
    try {
        findNavController(hostId).popBackStack()
    } catch (e: Exception) {
        Timber.d(e)
    }
}

fun AppCompatActivity.popBackStack(
    @IdRes hostId: Int,
    @IdRes destinationId: Int,
    inclusive: Boolean
) {
    try {
        findNavController(hostId).popBackStack(destinationId, inclusive)
    } catch (e: Exception) {
        Timber.d(e)
    }
}

fun AppCompatActivity.navigate(
    @IdRes hostId: Int,
    @IdRes idRes: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    try {
        findNavController(hostId).navigate(idRes, args, navOptions, navigatorExtras)
    } catch (e: Exception) {
        Timber.d(e)
    }
}

fun AppCompatActivity.navigate(
    @IdRes hostId: Int,
    uri: Uri,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    try {
        findNavController(hostId).navigate(uri, navOptions, navigatorExtras)
    } catch (e: Exception) {
        Timber.d(e)
    }
}

fun AppCompatActivity.navigate(
    @IdRes hostId: Int,
    request: NavDeepLinkRequest,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    try {
        findNavController(hostId).navigate(request, navOptions, navigatorExtras)
    } catch (e: Exception) {
        Timber.d(e)
    }
}

fun AppCompatActivity.navigate(
    @IdRes hostId: Int,
    directions: NavDirections,
    navOptions: NavOptions? = null
) {
    try {
        findNavController(hostId).navigate(directions, navOptions)
    } catch (e: Exception) {
        Timber.d(e)
    }
}

fun AppCompatActivity.navigate(
    @IdRes hostId: Int,
    directions: NavDirections,
    navigatorExtras: Navigator.Extras
) {
    try {
        findNavController(hostId).navigate(directions, navigatorExtras)
    } catch (e: Exception) {
        Timber.d(e)
    }
}

fun Fragment.mayNavigate(): Boolean {
    val actualDestinationId = findNavController().currentDestination?.id
    val thisFragmentDestinationId =
        view?.getTag(R.id.tag_navigation_destination_id) ?: actualDestinationId

    return if (actualDestinationId == thisFragmentDestinationId) {
        view?.setTag(R.id.tag_navigation_destination_id, thisFragmentDestinationId)
        true
    } else {
        false
    }
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.popBackStack(
    @IdRes destinationId: Int,
    inclusive: Boolean
) {
    findNavController().popBackStack(destinationId, inclusive)
}

fun Fragment.navigate(
    @IdRes idRes: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    checkCurrentDestination: Boolean = true,
) {
    if (mayNavigate() || !checkCurrentDestination)
        try {
            findNavController().navigate(idRes, args, navOptions, navigatorExtras)
        } catch (e: Exception) {
            Timber.d(e)
        }
}

fun Fragment.navigate(
    uri: Uri,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    checkCurrentDestination: Boolean = true,
) {
    if (mayNavigate() || !checkCurrentDestination)
        try {
            findNavController().navigate(uri, navOptions, navigatorExtras)
        } catch (e: Exception) {
            Timber.d(e)
        }
}

fun Fragment.navigate(
    request: NavDeepLinkRequest,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    checkCurrentDestination: Boolean = true,
) {
    if (mayNavigate() || !checkCurrentDestination)
        try {
            findNavController().navigate(request, navOptions, navigatorExtras)
        } catch (e: Exception) {
            Timber.d(e)
        }
}

fun Fragment.navigate(
    directions: NavDirections,
    navOptions: NavOptions? = null,
    checkCurrentDestination: Boolean = true,
) {
    if (mayNavigate() || !checkCurrentDestination)
        try {
            findNavController().navigate(directions, navOptions)
        } catch (e: Exception) {
            Timber.d(e)
        }
}

fun Fragment.navigate(
    directions: NavDirections,
    navigatorExtras: Navigator.Extras,
    checkCurrentDestination: Boolean = true,
) {
    if (mayNavigate() || !checkCurrentDestination)
        try {
            findNavController().navigate(directions, navigatorExtras)
        } catch (e: Exception) {
            Timber.d(e)
        }
}

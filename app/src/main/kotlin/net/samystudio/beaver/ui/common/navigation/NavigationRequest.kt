package net.samystudio.beaver.ui.common.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

sealed class NavigationRequest {
    class Pop(
        @param:IdRes @get:IdRes val destinationId: Int? = null,
        val inclusive: Boolean = false
    ) : NavigationRequest()

    class PopToRoot(val inclusive: Boolean = false) : NavigationRequest()
    class Push(
        @param:IdRes @get:IdRes val destinationId: Int,
        val args: Bundle? = null,
        val options: NavOptions? = null,
        val extras: Navigator.Extras? = null
    ) : NavigationRequest()

    object Finish : NavigationRequest()
}
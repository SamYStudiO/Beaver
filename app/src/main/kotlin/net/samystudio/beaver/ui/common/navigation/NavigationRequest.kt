package net.samystudio.beaver.ui.common.navigation

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import net.samystudio.beaver.ui.base.fragment.BaseFragment

sealed class NavigationRequest {
    class Pop(val destination: FragmentNavigator.Destination? = null) : NavigationRequest()
    object PopToRoot : NavigationRequest()
    class Push(
        val destination: FragmentNavigator.Destination,
        val args: Bundle? = null,
        val options: NavOptions? = null
    ) : NavigationRequest()

    class Dialog(
        val destination: BaseFragment,
        val tag: String? = null
    ) : NavigationRequest()
}
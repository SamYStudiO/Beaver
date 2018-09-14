package net.samystudio.beaver.ui.common.navigation

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController

interface Navigable {
    val navigationController: NavController

    fun handleNavigationRequest(
        navigationRequest: NavigationRequest,
        fragmentManager: FragmentManager
    ) {
        when (navigationRequest) {
            is NavigationRequest.Pop -> {
                if (navigationRequest.destination != null) navigationController.popBackStack(
                    navigationRequest.destination.id,
                    false
                )
                else navigationController.popBackStack()
            }
            is NavigationRequest.PopToRoot -> navigationController.popBackStack(
                navigationController.graph.startDestination,
                false
            )
            is NavigationRequest.Push -> navigationController.navigate(
                navigationRequest.destination.id,
                navigationRequest.args,
                navigationRequest.options
            )
            is NavigationRequest.Dialog -> {
                val tag: String =
                    navigationRequest.tag ?: navigationRequest.destination.javaClass.name
                navigationRequest.destination.show(fragmentManager, tag)
            }
        }
    }
}
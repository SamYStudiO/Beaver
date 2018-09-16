package net.samystudio.beaver.ext

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import net.samystudio.beaver.ui.common.navigation.NavigationRequest

fun NavController.navigate(request: NavigationRequest, fragmentManager: FragmentManager? = null) {
    when (request) {
        is NavigationRequest.Pop -> {
            if (request.destination != null) popBackStack(
                request.destination.id,
                false
            )
            else popBackStack()
        }
        is NavigationRequest.PopToRoot -> popBackStack(
            graph.startDestination,
            false
        )
        is NavigationRequest.Push -> navigate(
            request.destination.id,
            request.args,
            request.options
        )
        is NavigationRequest.Dialog -> {
            val tag: String =
                request.tag ?: request.destination.javaClass.name

            if (fragmentManager == null) throw IllegalArgumentException("You cannot navigate to a Dialog using NavController.navigate with a null fragmentManager argument")
            request.destination.show(fragmentManager, tag)
        }
    }
}
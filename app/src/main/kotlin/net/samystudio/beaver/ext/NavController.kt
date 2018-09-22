package net.samystudio.beaver.ext

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import net.samystudio.beaver.ui.common.navigation.NavigationRequest

fun NavController.navigate(request: NavigationRequest, fragmentManager: FragmentManager? = null) {
    when (request) {
        is NavigationRequest.Pop -> {
            if (request.destinationId != null && (request.destinationId > 0)) popBackStack(
                request.destinationId,
                false
            )
            else popBackStack()
        }
        is NavigationRequest.PopToRoot -> popBackStack(
            graph.startDestination,
            false
        )
        is NavigationRequest.Push -> navigate(
            request.destinationId,
            request.args,
            request.options,
            request.extras
        )
        is NavigationRequest.Dialog -> {
            val tag: String =
                request.tag ?: request.destination.getClassTag()

            if (fragmentManager == null) throw IllegalArgumentException("You cannot navigate to a Dialog using NavController.navigate with a null fragmentManager argument")
            request.destination.show(fragmentManager, tag)
        }
    }
}
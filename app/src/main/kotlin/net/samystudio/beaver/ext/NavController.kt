package net.samystudio.beaver.ext

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import net.samystudio.beaver.ui.common.navigation.NavigationRequest

fun NavController.navigate(
    request: NavigationRequest,
    activity: FragmentActivity? = null
) {
    when (request) {
        is NavigationRequest.Pop -> {
            if (request.destinationId != null && (request.destinationId > 0))
                popBackStack(request.destinationId, request.inclusive)
            else
                popBackStack()
        }
        is NavigationRequest.PopToRoot -> popBackStack(graph.startDestination, request.inclusive)
        is NavigationRequest.Push -> navigate(
            request.destinationId,
            request.args,
            request.options,
            request.extras
        )
        is NavigationRequest.PushDeepLink -> navigate(
            request.deepLink,
            request.options,
            request.extras
        )
        is NavigationRequest.PushDirection -> request.options?.let {
            navigate(
                request.direction,
                it
            )
        } ?: request.extras?.let {
            navigate(
                request.direction,
                it
            )
        } ?: navigate(request.direction)
        is NavigationRequest.Finish -> {
            requireNotNull(activity) { "You cannot finish an Activity using NavController.navigate with a null activity argument" }
            activity.finish()
        }
    }
}
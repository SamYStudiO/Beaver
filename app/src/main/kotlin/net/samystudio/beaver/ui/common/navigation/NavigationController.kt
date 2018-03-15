package net.samystudio.beaver.ui.common.navigation

interface NavigationController
{
    val fragmentNavigationManager: FragmentNavigationManager

    fun handleNavigationRequest(navigationRequest: NavigationRequest)
    {
        when (navigationRequest)
        {
            is NavigationRequest.ActivityRequest    -> fragmentNavigationManager.startActivity(
                navigationRequest)
            is NavigationRequest.FragmentRequest<*> -> fragmentNavigationManager.startFragment(
                navigationRequest)
            is NavigationRequest.BackStackRequest   -> fragmentNavigationManager.popBackStack(
                navigationRequest)
            is NavigationRequest.ClearStackRequest  -> fragmentNavigationManager.clearBackStack()
            is NavigationRequest.UrlRequest         -> fragmentNavigationManager.startUrl(
                navigationRequest.uri)
        }
    }
}
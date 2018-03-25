package net.samystudio.beaver.ui.common.navigation

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction

sealed class NavigationRequest
{
    class Pop(val controller: Controller? = null) : NavigationRequest()
    class PopToRoot : NavigationRequest()
    class Push(val transaction: RouterTransaction) : NavigationRequest()
    class ReplaceTop(val transaction: RouterTransaction) : NavigationRequest()
}
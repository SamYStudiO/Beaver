package net.samystudio.beaver.ui.common.navigation

import com.bluelinelabs.conductor.RouterTransaction
import net.samystudio.beaver.ui.base.controller.BaseController

sealed class NavigationRequest
{
    class Pop(val controller: BaseController? = null) : NavigationRequest()
    class PopToRoot : NavigationRequest()
    class Push(val transaction: RouterTransaction) : NavigationRequest()
    class ReplaceTop(val transaction: RouterTransaction) : NavigationRequest()
    class Dialog(val controller: BaseController, val transaction: RouterTransaction? = null) :
        NavigationRequest()
}
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseActivityViewModel
constructor(override val fragmentNavigationManager: FragmentNavigationManager) : BaseViewModel(),
                                                                                 ViewControllerViewModel

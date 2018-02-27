@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import net.samystudio.beaver.ui.common.navigation.FragmentNavigation
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseFragmentViewModel constructor(
    /**
     * @hide
     */
    override val fragmentNavigationManager: FragmentNavigationManager) : BaseViewModel(),
                                                                         FragmentNavigation
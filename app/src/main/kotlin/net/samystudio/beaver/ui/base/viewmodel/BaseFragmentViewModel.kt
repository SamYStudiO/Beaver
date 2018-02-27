@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.common.navigation.FragmentNavigation
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

@FragmentScope
abstract class BaseFragmentViewModel constructor(
    /**
     * @hide
     */
    @Inject
    override val fragmentNavigationManager: FragmentNavigationManager) : BaseViewModel(),
                                                                         FragmentNavigation
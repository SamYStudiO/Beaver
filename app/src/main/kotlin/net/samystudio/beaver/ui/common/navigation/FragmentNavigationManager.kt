package net.samystudio.beaver.ui.common.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.IntRange
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment

/**
 * Navigation manager for [BaseFragment]. It is strongly recommended to do all fragment request here.
 */
class FragmentNavigationManager
/**
 * @param fragmentContainerViewId The fragment container view id used to display [BaseFragment]
 * screens. Usually a [android.widget.FrameLayout] from your MainActivity layout place between your
 * [android.support.v7.widget.Toolbar] and eventually a bottom navigation or any kind of footer.
 */
constructor(activity: AppCompatActivity,
            private val fragmentManager: FragmentManager,
            @IdRes
            private val fragmentContainerViewId: Int) :
    ActivityNavigationManager(activity)
{
    /**
     * Get current [BaseFragment] screen from container view id specified when building this
     * [FragmentNavigationManager] instance. May be null if no request already occurred.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseFragment> getCurrentFragment(): T? =
        fragmentManager.findFragmentById(fragmentContainerViewId) as T?

    /**
     * Show specified [BaseFragment] screen from an instance and an optional bundle.
     *
     * @see BaseFragment.setArguments
     */
    fun <T : BaseFragment> startFragment(fragment: T,
                                         bundle: Bundle? = null,
                                         addToBackStack: Boolean = true,
                                         forResultRequestCode: Int? = null) =
        startFragment(FragmentNavigationRequest.Builder(fragment)
                          .addToBackStack(addToBackStack)
                          .bundle(bundle)
                          .build(), forResultRequestCode)

    /**
     * Show specified [BaseFragment] screen from a [Class] and an optional bundle.
     *
     * @see BaseFragment.setArguments
     */
    fun <T : BaseFragment> startFragment(fragmentClass: Class<T>,
                                         bundle: Bundle? = null,
                                         addToBackStack: Boolean = true,
                                         forResultRequestCode: Int? = null) =
        startFragment(FragmentNavigationRequest.Builder(context, fragmentClass)
                          .addToBackStack(addToBackStack)
                          .bundle(bundle)
                          .build(),
                      forResultRequestCode)

    /**
     * Show a [BaseFragment] screen using a [FragmentNavigationRequest]. This is
     * an advanced way with more parameters.
     *
     * @see FragmentNavigationRequest.Builder
     */
    @SuppressLint("CommitTransaction")
    fun <T : BaseFragment> startFragment(fragmentNavigationRequest: FragmentNavigationRequest<T>,
                                         forResultRequestCode: Int? = null): FragmentNavigationRequest<T>
    {
        val currentFragment: BaseFragment? = getCurrentFragment()

        if (forResultRequestCode != null && currentFragment != null)
            fragmentNavigationRequest.fragment.setTargetFragment(currentFragment,
                                                                 forResultRequestCode)

        val transaction =
            fragmentNavigationRequest.prepareTransaction(fragmentManager.beginTransaction())

        transaction
            .setReorderingAllowed(true)
            .replace(fragmentContainerViewId, fragmentNavigationRequest.fragment)
            .commit()

        return fragmentNavigationRequest
    }

    /**
     * Pop all stack from [FragmentManager].
     *
     * @see FragmentManager.popBackStack
     */
    fun clearBackStack(): Boolean
    {
        if (fragmentManager.backStackEntryCount > 0)
        {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).name,
                                         FragmentManager.POP_BACK_STACK_INCLUSIVE)
            return true
        }

        return false
    }

    /**
     * Pop stack from [FragmentManager].
     *
     * @see FragmentManager.popBackStack
     */
    fun popBackStack(): Boolean
    {
        if (fragmentManager.backStackEntryCount > 0)
        {
            fragmentManager.popBackStack()
            return true
        }

        return false
    }

    /**
     * Pop stack from [FragmentManager].
     *
     * @see FragmentManager.popBackStack
     */
    fun popBackStack(tag: String,
                     flags: Int): Boolean
    {
        if (fragmentManager.findFragmentByTag(tag) != null)
        {
            fragmentManager.popBackStack(tag, flags)
            return true
        }

        return false
    }

    /**
     * Pop stack with the specified offset. If you want to go back several screens from current one,
     * use this with the offset you want to go back. A offset of 1 is equivalent to [popBackStack]
     * (it only go back one screen back from current one).
     */
    fun popBackStack(@IntRange(from = 1) offset: Int): Boolean
    {
        val count = fragmentManager.backStackEntryCount

        return if (count <= offset) clearBackStack()
        else popBackStack(fragmentManager.getBackStackEntryAt(count - offset).name,
                          FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
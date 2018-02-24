@file:Suppress("MemberVisibilityCanBePrivate", "MemberVisibilityCanPrivate")

package net.samystudio.beaver.ui.common.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

/**
 * Navigation manager for [Fragment] used as screen and for [DialogFragment]. It is strongly
 * recommended to do all fragment request here to easily managed [FragmentManager] state loss and
 * thus avoid `java.lang.IllegalStateException: Can not perform this action after
 * onSaveInstanceState`.
 */
class FragmentNavigationManager
/**
 * @param fragmentContainerViewId The fragment container view id used to display [Fragment] screens.
 * Usually a [android.widget.FrameLayout] from your MainActivity layout place between your
 * [android.support.v7.widget.Toolbar] and eventually a bottom navigation or any kind of footer.
 */
@JvmOverloads
constructor(private val context: Context,
            private val fragmentManager: FragmentManager,
            @param:IdRes @field:IdRes
            private val fragmentContainerViewId: Int,
            @param:StateLossPolicy @field:StateLossPolicy
            var defaultStateLossPolicy: Long = STATE_LOSS_POLICY_ALLOW)
{

    /**
     * Get current [Fragment] screen from container view id specified when building this
     * [FragmentNavigationManager] instance. May be null if no request already occurred.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Fragment> getCurrentFragment(): T? =
        fragmentManager.findFragmentById(fragmentContainerViewId) as T

    /**
     * Show specified [Fragment] screen or [DialogFragment] from an instance and an optional bundle.
     *
     * @see Fragment.setArguments
     */
    @JvmOverloads
    fun <T : Fragment> showFragment(fragment: T, bundle: Bundle? = null) =
        showFragment(FragmentNavigationRequest(fragment, bundle))

    /**
     * Show specified [Fragment] screen or [DialogFragment] from a [Class] and an optional bundle.
     *
     * @see Fragment.setArguments
     */
    @JvmOverloads
    fun <T : Fragment> showFragment(fragmentClass: Class<T>, bundle: Bundle? = null) =
        showFragment(FragmentNavigationRequest(context, fragmentClass, bundle))

    /**
     * Show a [Fragment] screen or a [DialogFragment] using a [FragmentNavigationRequest]. This is
     * an advanced way with more parameters.
     *
     * @see FragmentNavigationRequest.Builder
     */
    @SuppressLint("CommitTransaction")
    fun <T : Fragment> showFragment(
        fragmentNavigationRequest: FragmentNavigationRequest<T>): FragmentNavigationRequest<T>
    {
        if (fragmentManager.isStateSaved && fragmentNavigationRequest.getStateLossPolicy(
                defaultStateLossPolicy) == STATE_LOSS_POLICY_CANCEL)
        {
            fragmentNavigationRequest.isCancelled = true
            return fragmentNavigationRequest
        }

        fragmentNavigationRequest.isCancelled = false

        val transaction =
            fragmentNavigationRequest.prepareTransaction(fragmentManager.beginTransaction())

        if (fragmentNavigationRequest.isDialog)
            transaction.add(fragmentNavigationRequest.fragment,
                            fragmentNavigationRequest.tag)
        else
            transaction
                .setReorderingAllowed(true)
                .replace(fragmentContainerViewId, fragmentNavigationRequest.fragment)

        if (fragmentManager.isStateSaved && fragmentNavigationRequest.getStateLossPolicy(
                defaultStateLossPolicy) == STATE_LOSS_POLICY_ALLOW)
            transaction.commitAllowingStateLoss()
        else
            transaction.commit()

        return fragmentNavigationRequest
    }

    /**
     * Pop all stack from [FragmentManager], doing it immediately if immediate argument is true and
     * ignoring request (return false) if state loss policy is different than [STATE_LOSS_POLICY_IGNORE].
     *
     * @see FragmentManager.popBackStack
     * @see FragmentManager.popBackStackImmediate
     */
    @JvmOverloads
    fun clearBackStack(immediate: Boolean = false,
                       @StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

        if (fragmentManager.backStackEntryCount > 0)
        {
            if (immediate)
                fragmentManager.popBackStackImmediate(fragmentManager
                                                          .getBackStackEntryAt(0)
                                                          .name,
                                                      FragmentManager.POP_BACK_STACK_INCLUSIVE)
            else
                fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).name,
                                             FragmentManager.POP_BACK_STACK_INCLUSIVE)
            return true
        }

        return false
    }

    /**
     * Pop stack from [FragmentManager], doing it immediately if immediate argument is true and
     * ignoring request (return false) if state loss policy is different than
     * [STATE_LOSS_POLICY_IGNORE].
     *
     * @see FragmentManager.popBackStack
     * @see FragmentManager.popBackStackImmediate
     */
    @JvmOverloads
    fun popBackStack(immediate: Boolean = false,
                     @StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

        if (fragmentManager.backStackEntryCount > 0)
        {
            if (immediate)
                return fragmentManager.popBackStackImmediate()
            else
                fragmentManager.popBackStack()
            return true
        }

        return false
    }

    /**
     * Pop stack from [FragmentManager], doing it immediately if immediate argument is true and
     * ignoring request (return false) if state loss policy is different than
     * [STATE_LOSS_POLICY_IGNORE].
     *
     * @see FragmentManager.popBackStack
     * @see FragmentManager.popBackStackImmediate
     */
    @JvmOverloads
    fun popBackStack(tag: String,
                     flags: Int,
                     immediate: Boolean = false,
                     @StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

        if (fragmentManager.findFragmentByTag(tag) != null)
        {
            if (immediate)
                return fragmentManager.popBackStackImmediate(tag, flags)
            else
                fragmentManager.popBackStack(tag, flags)
            return true
        }

        return false
    }

    /**
     * Pop stack with the specified offset. If you want to go back several screens from current one,
     * use this with the offset you want to go back. A offset of 1 is equivalent to [popBackStack]
     * (it only go back one screen back from current one).
     * Request is ignored (return false) if state loss policy is different than
     * [STATE_LOSS_POLICY_IGNORE].
     */
    @JvmOverloads
    fun popBackStack(@IntRange(from = 1) offset: Int,
                     immediate: Boolean = false,
                     @StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

        val count = fragmentManager.backStackEntryCount

        return if (count <= offset)
        {
            clearBackStack(immediate)
        }
        else popBackStack(fragmentManager.getBackStackEntryAt(count - offset).name,
                          FragmentManager.POP_BACK_STACK_INCLUSIVE,
                          immediate)
    }

    /**
     * @see FragmentNavigationRequest.getStateLossPolicy
     */
    @JvmOverloads
    fun dismissDialog(dialog: DialogFragment,
                      @StateLossPolicy stateLossPolicy: Long? = defaultStateLossPolicy) =
        dismissDialog(FragmentNavigationRequest.Builder(dialog)
                          .stateLossPolicy(stateLossPolicy ?: defaultStateLossPolicy)
                          .build())

    /**
     * @see FragmentNavigationRequest.tag
     * @see FragmentNavigationRequest.getStateLossPolicy
     */
    @JvmOverloads
    fun dismissDialog(tag: String, @StateLossPolicy
    stateLossPolicy: Long? = defaultStateLossPolicy): Boolean
    {
        val dialog: DialogFragment? = fragmentManager.findFragmentByTag(tag) as DialogFragment

        @Suppress("FoldInitializerAndIfToElvis")
        if (dialog == null) throw IllegalArgumentException("Can't find dialog fragment with tag " + tag)

        return dismissDialog(FragmentNavigationRequest.Builder(dialog)
                                 .stateLossPolicy(stateLossPolicy ?: defaultStateLossPolicy)
                                 .build())
    }

    private fun dismissDialog(fragmentNavigationRequest: FragmentNavigationRequest<out DialogFragment>): Boolean
    {
        val dialog = fragmentNavigationRequest.fragment

        if ((fragmentManager.isStateSaved &&
                    fragmentNavigationRequest.getStateLossPolicy(defaultStateLossPolicy) == STATE_LOSS_POLICY_CANCEL) ||
            (dialog.dialog != null && !dialog.dialog.isShowing))
        {
            fragmentNavigationRequest.isCancelled = true
            return false
        }

        fragmentNavigationRequest.isCancelled = false

        val transaction = fragmentManager.beginTransaction()
        transaction.remove(dialog)

        if (fragmentManager.isStateSaved && fragmentNavigationRequest.getStateLossPolicy(
                defaultStateLossPolicy) == STATE_LOSS_POLICY_ALLOW)
            transaction.commitAllowingStateLoss()
        else
            transaction.commit()

        return true
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STATE_LOSS_POLICY_IGNORE,
            STATE_LOSS_POLICY_CANCEL,
            STATE_LOSS_POLICY_ALLOW,
            STATE_LOSS_POLICY_UNSET)
    annotation class StateLossPolicy

    companion object
    {
        /**
         * Policy indicating to allow request if [FragmentManager] state is already saved, request
         * will use [FragmentTransaction.commitAllowingStateLoss] or
         * [DialogFragment.dismissAllowingStateLoss], if request is clearing or popping back stack
         * this will be ignore since there is no way to pop back stack allowing state loss.
         */
        const val STATE_LOSS_POLICY_ALLOW = 1L

        /**
         * Policy indicating to cancel request if [FragmentManager] state is already saved.
         */
        const val STATE_LOSS_POLICY_CANCEL = 2L

        /**
         * Policy indicating to ignore [FragmentManager] state, request will use
         * [FragmentTransaction.commit] or [DialogFragment.dismiss] regardless of current state.
         */
        const val STATE_LOSS_POLICY_IGNORE = 3L

        /**
         * @hide
         */
        const val STATE_LOSS_POLICY_UNSET = 0L
    }
}
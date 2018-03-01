@file:Suppress("MemberVisibilityCanBePrivate", "MemberVisibilityCanPrivate")

package net.samystudio.beaver.ui.common.navigation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import net.samystudio.beaver.di.qualifier.FragmentContainerViewId
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.fragment.dialog.BaseDialog
import javax.inject.Inject

/**
 * Navigation manager for [BaseFragment] used as screen and for [BaseDialog]. It is strongly
 * recommended to do all fragment request here to easily managed [FragmentManager] state loss and
 * thus avoid `java.lang.IllegalStateException: Can not perform this action after
 * onSaveInstanceState`.
 */
@ActivityScope
class FragmentNavigationManager
/**
 * @param fragmentContainerViewId The fragment container view id used to display [BaseFragment]
 * screens. Usually a [android.widget.FrameLayout] from your MainActivity layout place between your
 * [android.support.v7.widget.Toolbar] and eventually a bottom navigation or any kind of footer.
 */
@Inject
constructor(activity: AppCompatActivity,
            val fragmentManager: FragmentManager,
            @param:FragmentContainerViewId @IdRes
            private val fragmentContainerViewId: Int) :
    ActivityNavigationManager(activity)
{
    @StateLossPolicy
    var defaultStateLossPolicy: Long = STATE_LOSS_POLICY_ALLOW

    /**
     * Get current [BaseFragment] screen from container view id specified when building this
     * [FragmentNavigationManager] instance. May be null if no request already occurred.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseFragment<*>> getCurrentFragment(): T? =
        fragmentManager.findFragmentById(fragmentContainerViewId) as T?

    /**
     * Show specified [BaseFragment] screen from an instance and an optional bundle.
     *
     * @see BaseFragment.setArguments
     */
    fun <T : BaseFragment<*>> startFragment(fragment: T,
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
    fun <T : BaseFragment<*>> startFragment(fragmentClass: Class<T>,
                                            bundle: Bundle? = null,
                                            addToBackStack: Boolean = true,
                                            forResultRequestCode: Int? = null) =
        startFragment(FragmentNavigationRequest.Builder(context, fragmentClass)
                          .addToBackStack(addToBackStack)
                          .bundle(bundle)
                          .build(),
                      forResultRequestCode)

    /**
     * Show a [BaseFragment] screen or a [BaseDialog] using a [FragmentNavigationRequest]. This is
     * an advanced way with more parameters.
     *
     * @see FragmentNavigationRequest.Builder
     */
    @SuppressLint("CommitTransaction")
    fun <T : BaseFragment<*>> startFragment(fragmentNavigationRequest: FragmentNavigationRequest<T>,
                                            forResultRequestCode: Int? = null): FragmentNavigationRequest<T>
    {
        if (fragmentManager.isStateSaved && fragmentNavigationRequest.getStateLossPolicy(
                defaultStateLossPolicy) == STATE_LOSS_POLICY_CANCEL)
        {
            fragmentNavigationRequest.isCancelled = true
            return fragmentNavigationRequest
        }

        fragmentNavigationRequest.isCancelled = false

        val currentFragment: BaseFragment<*>? = getCurrentFragment()
        if (forResultRequestCode != null && currentFragment != null)
            fragmentNavigationRequest.fragment.setTargetFragment(currentFragment,
                                                                 forResultRequestCode)

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
     * Pop all stack from [FragmentManager], ignoring request (return false) if state loss policy is
     * different than [STATE_LOSS_POLICY_IGNORE] and fragment manager state is saved.
     *
     * @see FragmentManager.popBackStack
     */
    fun clearBackStack(@StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

        if (fragmentManager.backStackEntryCount > 0)
        {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).name,
                                         FragmentManager.POP_BACK_STACK_INCLUSIVE)
            return true
        }

        return false
    }

    /**
     * Pop stack from [FragmentManager], ignoring request (return false) if state loss policy is
     * different than [STATE_LOSS_POLICY_IGNORE] and fragment manager state is saved.
     *
     * @see FragmentManager.popBackStack
     */
    fun popBackStack(@StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

        if (fragmentManager.backStackEntryCount > 0)
        {
            fragmentManager.popBackStack()
            return true
        }

        return false
    }

    /**
     * Pop stack from [FragmentManager], ignoring request (return false) if state loss policy is
     * different than [STATE_LOSS_POLICY_IGNORE] and fragment manager state is saved.
     *
     * @see FragmentManager.popBackStack
     */
    fun popBackStack(tag: String,
                     flags: Int,
                     @StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

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
     * Request is ignored (return false) if state loss policy is different than
     * [STATE_LOSS_POLICY_IGNORE] and fragment manager state is saved.
     */
    fun popBackStack(@IntRange(from = 1) offset: Int,
                     @StateLossPolicy stateLossPolicy: Long = defaultStateLossPolicy): Boolean
    {
        if (fragmentManager.isStateSaved && stateLossPolicy != STATE_LOSS_POLICY_IGNORE)
            return false

        val count = fragmentManager.backStackEntryCount

        return if (count <= offset) clearBackStack()
        else popBackStack(fragmentManager.getBackStackEntryAt(count - offset).name,
                          FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    /**
     * @see FragmentNavigationRequest.getStateLossPolicy
     */
    fun dismissDialog(dialog: BaseDialog<*>,
                      @StateLossPolicy stateLossPolicy: Long? = defaultStateLossPolicy) =
        dismissDialog(FragmentNavigationRequest.Builder(dialog)
                          .stateLossPolicy(stateLossPolicy ?: defaultStateLossPolicy)
                          .build())

    /**
     * @see FragmentNavigationRequest.tag
     * @see FragmentNavigationRequest.getStateLossPolicy
     */
    fun dismissDialog(tag: String, @StateLossPolicy
    stateLossPolicy: Long? = defaultStateLossPolicy): Boolean
    {
        val dialog: BaseDialog<*> =
            fragmentManager.findFragmentByTag(tag) as BaseDialog<*>? ?: return false

        return dismissDialog(FragmentNavigationRequest.Builder(dialog)
                                 .stateLossPolicy(stateLossPolicy ?: defaultStateLossPolicy)
                                 .build())
    }

    private fun dismissDialog(fragmentNavigationRequest: FragmentNavigationRequest<out BaseDialog<*>>): Boolean
    {
        val dialogFragment: BaseDialog<*> = fragmentNavigationRequest.fragment
        val dialog: Dialog? = dialogFragment.dialog

        if ((fragmentManager.isStateSaved &&
                    fragmentNavigationRequest.getStateLossPolicy(defaultStateLossPolicy) == STATE_LOSS_POLICY_CANCEL) ||
            (dialog != null && !dialog.isShowing))
        {
            fragmentNavigationRequest.isCancelled = true
            return false
        }

        fragmentNavigationRequest.isCancelled = false

        val transaction = fragmentManager.beginTransaction()
        transaction.remove(dialogFragment)

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
            STATE_LOSS_POLICY_ALLOW)
    annotation class StateLossPolicy

    companion object
    {
        /**
         * Policy indicating to allow request if [FragmentManager] state is already saved, request
         * will use [FragmentTransaction.commitAllowingStateLoss] if request is clearing or popping
         * back stack this will be ignore since there is no way to pop back stack allowing state loss.
         */
        const val STATE_LOSS_POLICY_ALLOW = 0L

        /**
         * Policy indicating to cancel request if [FragmentManager] state is already saved.
         */
        const val STATE_LOSS_POLICY_CANCEL = 1L

        /**
         * Policy indicating to ignore [FragmentManager] state, request will use
         * [FragmentTransaction.commit] regardless of current state.
         */
        const val STATE_LOSS_POLICY_IGNORE = 2L
    }
}
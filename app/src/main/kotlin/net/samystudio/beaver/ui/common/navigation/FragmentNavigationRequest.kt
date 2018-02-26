@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.common.navigation

import android.content.Context
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.annotation.AnimatorRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.fragment.dialog.BaseDialog
import java.util.*

/**
 * Advanced [Fragment] or [DialogFragment] request to use along with
 * [FragmentNavigationManager.startFragment].
 */
class FragmentNavigationRequest<T : BaseFragment> constructor(builder: Builder<T>)
{
    val fragment: T

    /**
     * Get [Bundle] that will be inject (or added if one already exists) to [BaseFragment] when
     * request is executed.
     *
     * @see BaseFragment.setArguments
     */
    val bundle: Bundle?

    /**
     * Get if this request will be added to back stack when request is executed. Note this is always
     * false for a [BaseDialog] request.
     */
    val addToBackStack: Boolean

    @FragmentNavigationManager.StateLossPolicy
    val stateLossPolicy: Long?

    /**
     * Get which policy should be used when executing this request after
     * [android.support.v4.app.FragmentManager] state is saved.
     *
     * @param defaultStateLossPolicy A default state loss policy to replace actual policy if it's null
     * @see FragmentNavigationManager.STATE_LOSS_POLICY_IGNORE
     * @see FragmentNavigationManager.STATE_LOSS_POLICY_CANCEL
     * @see FragmentNavigationManager.STATE_LOSS_POLICY_ALLOW
     */
    @FragmentNavigationManager.StateLossPolicy
    fun getStateLossPolicy(@FragmentNavigationManager.StateLossPolicy
                           defaultStateLossPolicy: Long) =
        stateLossPolicy ?: defaultStateLossPolicy

    /**
     * Tag use for different purposes (see links below).
     *
     * @see android.support.v4.app.FragmentManager.findFragmentByTag
     * @see FragmentNavigationManager.popBackStack
     * @see FragmentNavigationManager.dismissDialog
     */
    val tag: String

    /**
     * @see FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val enterAnimRes: Int

    /**
     * @see FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val exitAnimRes: Int

    /**
     * @see FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val popEnterAnimRes: Int

    /**
     * @see FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val popExitAnimRes: Int

    /**
     * @see FragmentTransaction.addSharedElement
     */
    val sharedElementSourceView: List<View>

    /**
     * @see FragmentTransaction.addSharedElement
     */
    val sharedElementTargetNames: List<String>

    /**
     * @see FragmentTransaction.setTransition
     */
    val transition: Int

    /**
     * @see FragmentTransaction.setTransitionStyle
     */
    @StyleRes
    val transitionStyle: Int

    /**
     * @see FragmentTransaction.setBreadCrumbTitle
     */
    @StringRes
    val breadCrumbTitleRes: Int

    /**
     * @see FragmentTransaction.setBreadCrumbTitle
     */
    val breadCrumbTitle: CharSequence?

    /**
     * @see FragmentTransaction.setBreadCrumbShortTitle
     */
    @StringRes
    val breadCrumbShortTitleRes: Int
    private val mBreadCrumbShortTitle: CharSequence?

    /**
     * Get if this request was cancelled when last executed.
     */
    var isCancelled: Boolean = false

    var backStackId: Int = -1

    /**
     * Get if this is a [BaseDialog] or standard [BaseFragment] request.
     */
    val isDialog: Boolean
        get() = fragment is BaseDialog

    @JvmOverloads
    constructor(fragment: T, bundle: Bundle? = null) :
            this(Builder<T>(fragment).bundle(bundle))

    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    constructor(context: Context,
                fragmentClass: Class<T>,
                bundle: Bundle? = null) :
            this(Fragment.instantiate(context, fragmentClass.name) as T, bundle)

    init
    {
        fragment = builder.fragment
        bundle = builder.bundle
        addToBackStack = builder.addToBackStack
        stateLossPolicy = builder.stateLossPolicy
        tag = builder.tag ?: fragment.javaClass.name
        enterAnimRes = builder.enterAnimRes
        exitAnimRes = builder.exitAnimRes
        popEnterAnimRes = builder.popEnterAnimRes
        popExitAnimRes = builder.popExitAnimRes
        sharedElementSourceView = builder.sharedElementSourceView
        sharedElementTargetNames = builder.sharedElementTargetNames
        transition = builder.transition
        transitionStyle = builder.transitionStyle
        breadCrumbTitleRes = builder.breadCrumbTitleRes
        breadCrumbTitle = builder.breadCrumbTitle
        breadCrumbShortTitleRes = builder.breadCrumbShortTitleRes
        mBreadCrumbShortTitle = builder.breadCrumbShortTitle

        if (bundle != null)
        {
            if (fragment.arguments != null)
                fragment.arguments?.putAll(bundle)
            else
                fragment.arguments = bundle
        }
    }

    /**
     * @see FragmentTransaction.setBreadCrumbShortTitle
     */
    fun breadCrumbShortTitle() = mBreadCrumbShortTitle

    internal fun prepareTransaction(transaction: FragmentTransaction): FragmentTransaction
    {
        transaction
            .setCustomAnimations(enterAnimRes,
                                 exitAnimRes,
                                 popEnterAnimRes,
                                 popExitAnimRes)
            .setTransition(transition)
            .setTransitionStyle(transitionStyle)
            .setBreadCrumbTitle(breadCrumbTitleRes)
            .setBreadCrumbTitle(breadCrumbTitle)
            .setBreadCrumbShortTitle(breadCrumbShortTitleRes)
            .setBreadCrumbShortTitle(mBreadCrumbShortTitle)

        val size = sharedElementSourceView.size

        for (i in 0 until size)
        {
            transaction.addSharedElement(sharedElementSourceView[i],
                                         sharedElementTargetNames[i])
        }

        if (addToBackStack && !isDialog)
            transaction.addToBackStack(tag)

        return transaction
    }

    class Builder<T : BaseFragment>(val fragment: T)
    {
        var bundle: Bundle? = null
            private set
        var addToBackStack: Boolean = true
            private set
        var tag: String? = null
            private set
        @FragmentNavigationManager.StateLossPolicy
        var stateLossPolicy: Long? = null
            private set
        @AnimatorRes
        @AnimRes
        var enterAnimRes: Int = 0
            private set
        @AnimatorRes
        @AnimRes
        var exitAnimRes: Int = 0
            private set
        @AnimatorRes
        @AnimRes
        var popEnterAnimRes: Int = 0
            private set
        @AnimatorRes
        @AnimRes
        var popExitAnimRes: Int = 0
            private set
        val sharedElementSourceView = ArrayList<View>()
        val sharedElementTargetNames = ArrayList<String>()
        var transition: Int = 0
            private set
        @StyleRes
        var transitionStyle: Int = 0
            private set
        @StringRes
        var breadCrumbTitleRes: Int = 0
            private set
        var breadCrumbTitle: CharSequence? = null
            private set
        @StringRes
        var breadCrumbShortTitleRes: Int = 0
            private set
        var breadCrumbShortTitle: CharSequence? = null
            private set

        /**
         * Initiate a builder with a [BaseFragment] class instance.
         */
        @Suppress("UNCHECKED_CAST")
        constructor(context: Context, fragmentClass: Class<T>) :
                this(Fragment.instantiate(context, fragmentClass.name) as T)

        /**
         * Any [Bundle] that will be inject (or added if one already exists) to [BaseFragment] when
         * request is executed.
         *
         * @see BaseFragment.setArguments
         */
        fun bundle(bundle: Bundle?): Builder<T>
        {
            this.bundle = bundle
            return this
        }

        /**
         * Note back stack tag is automatically generated, if you want to specify one you may use
         * [tag]. Not also this has no effect for a [BaseDialog] request.
         *
         * @see FragmentTransaction.addToBackStack
         */
        fun addToBackStack(addToBackStack: Boolean): Builder<T>
        {
            this.addToBackStack = addToBackStack
            return this
        }

        /**
         * Tag use for different purposes :
         *
         * @see android.support.v4.app.FragmentManager.findFragmentByTag
         * @see FragmentNavigationManager.popBackStack
         * @see FragmentNavigationManager.dismissDialog
         */
        fun tag(tag: String?): Builder<T>
        {
            this.tag = tag
            return this
        }

        /**
         * Specify which policy should be used when executing this request after
         * [android.support.v4.app.FragmentManager] state is saved.
         *
         * @see FragmentNavigationManager.STATE_LOSS_POLICY_IGNORE
         * @see FragmentNavigationManager.STATE_LOSS_POLICY_CANCEL
         * @see FragmentNavigationManager.STATE_LOSS_POLICY_ALLOW
         */
        fun stateLossPolicy(@FragmentNavigationManager.StateLossPolicy
                            stateLossPolicy: Long): Builder<T>
        {
            this.stateLossPolicy = stateLossPolicy
            return this
        }

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        fun customAnimations(@AnimatorRes @AnimRes enter: Int,
                             @AnimatorRes @AnimRes exit: Int): Builder<T>
        {
            this.enterAnimRes = enter
            this.exitAnimRes = exit
            return this
        }

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        fun customAnimations(@AnimatorRes @AnimRes enter: Int,
                             @AnimatorRes @AnimRes exit: Int,
                             @AnimatorRes @AnimRes popEnter: Int,
                             @AnimatorRes @AnimRes popExit: Int): Builder<T>
        {
            this.enterAnimRes = enter
            this.exitAnimRes = exit
            this.popEnterAnimRes = popEnter
            this.popExitAnimRes = popExit
            return this
        }

        /**
         * Call multiple times to add several shared elements.
         *
         * @see FragmentTransaction.addSharedElement
         */
        fun sharedElement(sharedElement: View, name: String): Builder<T>
        {
            this.sharedElementSourceView.add(sharedElement)
            this.sharedElementTargetNames.add(name)
            return this
        }

        /**
         * @see FragmentTransaction.setTransition
         */
        fun transition(transit: Int): Builder<T>
        {
            this.transition = transit
            return this
        }

        /**
         * @see FragmentTransaction.setTransitionStyle
         */
        fun transitionStyle(@StyleRes styleRes: Int): Builder<T>
        {
            this.transitionStyle = styleRes
            return this
        }

        /**
         * @see FragmentTransaction.setBreadCrumbTitle
         */
        fun breadCrumbTitle(@StringRes res: Int): Builder<T>
        {
            this.breadCrumbShortTitleRes = res
            return this
        }

        /**
         * @see FragmentTransaction.setBreadCrumbTitle
         */
        fun breadCrumbTitle(text: CharSequence?): Builder<T>
        {
            this.breadCrumbTitle = text
            return this
        }

        /**
         * @see FragmentTransaction.setBreadCrumbShortTitle
         */
        fun breadCrumbShortTitle(@StringRes res: Int): Builder<T>
        {
            this.breadCrumbShortTitleRes = res
            return this
        }

        /**
         * @see FragmentTransaction.setBreadCrumbShortTitle
         */
        fun breadCrumbShortTitle(text: CharSequence?): Builder<T>
        {
            this.breadCrumbShortTitle = text
            return this
        }

        fun build() = FragmentNavigationRequest(this)
    }
}

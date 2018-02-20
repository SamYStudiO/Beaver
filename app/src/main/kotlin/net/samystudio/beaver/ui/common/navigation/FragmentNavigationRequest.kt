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
import java.util.*

/**
 * Advanced [Fragment] or [DialogFragment] request to use along with
 * [FragmentNavigationManager.showFragment].
 */
class FragmentNavigationRequest<T : Fragment> constructor(builder: Builder<T>)
{
    val fragment: T

    /**
     * Get [Bundle] that will be inject (or added if one already exists) to [Fragment] when request
     * is executed.
     *
     * @see Fragment.setArguments
     */
    val bundle: Bundle?

    /**
     * Get if this request will be added to back stack when request is executed. Note this is always
     * false for a [DialogFragment] request.
     */
    val isAddedToBackStack: Boolean

    @FragmentNavigationManager.StateLossPolicy
    val stateLossPolicy: Long

    /**
     * Get which policy should be used when executing this request after
     * [android.support.v4.app.FragmentManager] state is saved.
     *
     * @param defaultStateLossPolicy A default state loss policy to replace actual policy if it is
     * [FragmentNavigationManager.STATE_LOSS_POLICY_UNSET]
     * @see FragmentNavigationManager.STATE_LOSS_POLICY_IGNORE
     * @see FragmentNavigationManager.STATE_LOSS_POLICY_CANCEL
     * @see FragmentNavigationManager.STATE_LOSS_POLICY_ALLOW
     */
    @FragmentNavigationManager.StateLossPolicy
    fun getStateLossPolicy(@FragmentNavigationManager.StateLossPolicy
                           defaultStateLossPolicy: Long) =
            if (stateLossPolicy == FragmentNavigationManager.STATE_LOSS_POLICY_UNSET)
                defaultStateLossPolicy
            else
                stateLossPolicy

    /**
     * Tag use for different purposes (see links below).
     *
     * @see android.support.v4.app.FragmentManager.findFragmentByTag
     * @see FragmentNavigationManager.popBackStack
     * @see FragmentNavigationManager.dismissDialog
     */
    val tag: String

    /**
     * @see android.support.v4.app.FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val enterAnimRes: Int

    /**
     * @see android.support.v4.app.FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val exitAnimRes: Int

    /**
     * @see android.support.v4.app.FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val popEnterAnimRes: Int

    /**
     * @see android.support.v4.app.FragmentTransaction.setCustomAnimations
     */
    @AnimatorRes
    @AnimRes
    val popExitAnimRes: Int

    /**
     * @see android.support.v4.app.FragmentTransaction.addSharedElement
     */
    val sharedElementSourceView: List<View>

    /**
     * @see android.support.v4.app.FragmentTransaction.addSharedElement
     */
    val sharedElementTargetNames: List<String>

    /**
     * @see android.support.v4.app.FragmentTransaction.setTransition
     */
    val transition: Int

    /**
     * @see android.support.v4.app.FragmentTransaction.setTransitionStyle
     */
    @StyleRes
    @get:StyleRes
    val transitionStyle: Int

    /**
     * @see android.support.v4.app.FragmentTransaction.setBreadCrumbTitle
     */
    @StringRes
    @get:StringRes
    val breadCrumbTitleRes: Int

    /**
     * @see android.support.v4.app.FragmentTransaction.setBreadCrumbTitle
     */
    val breadCrumbTitle: CharSequence?

    /**
     * @see android.support.v4.app.FragmentTransaction.setBreadCrumbShortTitle
     */
    @StringRes
    @get:StringRes
    val breadCrumbShortTitleRes: Int
    private val mBreadCrumbShortTitle: CharSequence?

    /**
     * Get if this request was cancelled when last executed.
     */
    var isCancelled: Boolean = false

    /**
     * Get if this is a [DialogFragment] or standard [Fragment] request.
     */
    val isDialog: Boolean
        get() = fragment is DialogFragment

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
        isAddedToBackStack = builder.addToBackStack
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
     * @see android.support.v4.app.FragmentTransaction.setBreadCrumbShortTitle
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

        if (isAddedToBackStack && !isDialog)
            transaction.addToBackStack(tag)

        return transaction
    }

    class Builder<T : Fragment>(val fragment: T)
    {
        var bundle: Bundle? = null
            private set
        var addToBackStack: Boolean = false
            private set
        var tag: String? = null
            private set
        @FragmentNavigationManager.StateLossPolicy
        var stateLossPolicy: Long = 0
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
         * Initiate a builder with a [Fragment] class instance.
         */
        @Suppress("UNCHECKED_CAST")
        constructor(context: Context, fragmentClass: Class<T>) :
                this(Fragment.instantiate(context, fragmentClass.name) as T)

        /**
         * Any [Bundle] that will be inject (or added if one already exists) to [Fragment] when
         * request is executed.
         *
         * @see Fragment.setArguments
         */
        fun bundle(bundle: Bundle?): Builder<T>
        {
            this.bundle = bundle
            return this
        }

        /**
         * Note back stack tag is automatically generated, if you want to specify one you may use
         * [tag]. Not also this has no effect for a [DialogFragment] request.
         *
         * @see android.support.v4.app.FragmentTransaction.addToBackStack
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
        fun stateLossPolicy(
                @FragmentNavigationManager.StateLossPolicy stateLossPolicy: Long): Builder<T>
        {
            this.stateLossPolicy = stateLossPolicy
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setCustomAnimations
         */
        fun customAnimations(@AnimatorRes @AnimRes enter: Int,
                             @AnimatorRes @AnimRes exit: Int): Builder<T>
        {
            this.enterAnimRes = enter
            this.exitAnimRes = exit
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setCustomAnimations
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
         * @see android.support.v4.app.FragmentTransaction.addSharedElement
         */
        fun sharedElement(sharedElement: View, name: String): Builder<T>
        {
            this.sharedElementSourceView.add(sharedElement)
            this.sharedElementTargetNames.add(name)
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setTransition
         */
        fun transition(transit: Int): Builder<T>
        {
            this.transition = transit
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setTransitionStyle
         */
        fun transitionStyle(@StyleRes styleRes: Int): Builder<T>
        {
            this.transitionStyle = styleRes
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setBreadCrumbTitle
         */
        fun breadCrumbTitle(@StringRes res: Int): Builder<T>
        {
            this.breadCrumbShortTitleRes = res
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setBreadCrumbTitle
         */
        fun breadCrumbTitle(text: CharSequence?): Builder<T>
        {
            this.breadCrumbTitle = text
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setBreadCrumbShortTitle
         */
        fun breadCrumbShortTitle(@StringRes res: Int): Builder<T>
        {
            this.breadCrumbShortTitleRes = res
            return this
        }

        /**
         * @see android.support.v4.app.FragmentTransaction.setBreadCrumbShortTitle
         */
        fun breadCrumbShortTitle(text: CharSequence?): Builder<T>
        {
            this.breadCrumbShortTitle = text
            return this
        }

        fun build() = FragmentNavigationRequest(this)
    }
}

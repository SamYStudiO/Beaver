@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.common.navigation

import android.content.Context
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.annotation.AnimatorRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import net.samystudio.beaver.ui.base.fragment.BaseSimpleFragment
import java.util.*

/**
 * Advanced [Fragment] request to use along with [FragmentNavigationManager.startFragment].
 */
class FragmentNavigationRequest<T : BaseSimpleFragment>
constructor(builder: Builder<T>)
{
    val fragment: T

    /**
     * Get [Bundle] that will be inject (or added if one already exists) to [BaseSimpleFragment] when
     * request is executed.
     *
     * @see BaseSimpleFragment.setArguments
     */
    val bundle: Bundle?

    /**
     * Get if this request will be added to back stack when request is executed.
     */
    val addToBackStack: Boolean

    /**
     * Tag use for different purposes (see links below).
     *
     * @see android.support.v4.app.FragmentManager.findFragmentByTag
     * @see FragmentNavigationManager.popBackStack
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

    constructor(fragment: T, bundle: Bundle? = null) :
            this(Builder<T>(fragment).bundle(bundle))

    @Suppress("UNCHECKED_CAST")
    constructor(context: Context,
                fragmentClass: Class<T>,
                bundle: Bundle? = null) :
            this(Fragment.instantiate(context, fragmentClass.name) as T, bundle)

    init
    {
        fragment = builder.fragment
        bundle = builder.bundle
        addToBackStack = builder.addToBackStack
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

        if (addToBackStack)
            transaction.addToBackStack(tag)

        return transaction
    }

    class Builder<T : BaseSimpleFragment>(val fragment: T)
    {
        var bundle: Bundle? = null
            private set
        var addToBackStack: Boolean = true
            private set
        var tag: String? = null
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
         * Initiate a builder with a [BaseSimpleFragment] class instance.
         */
        @Suppress("UNCHECKED_CAST")
        constructor(context: Context, fragmentClass: Class<T>) :
                this(Fragment.instantiate(context, fragmentClass.name) as T)

        /**
         * Any [Bundle] that will be inject (or added if one already exists) to [BaseSimpleFragment] when
         * request is executed.
         *
         * @see BaseSimpleFragment.setArguments
         */
        fun bundle(bundle: Bundle?): Builder<T> = apply { this.bundle = bundle }

        /**
         * Note back stack tag is automatically generated, if you want to specify one you may use
         * [tag].
         *
         * @see FragmentTransaction.addToBackStack
         */
        fun addToBackStack(addToBackStack: Boolean): Builder<T> =
            apply { this.addToBackStack = addToBackStack }

        /**
         * Tag use for different purposes :
         *
         * @see android.support.v4.app.FragmentManager.findFragmentByTag
         * @see FragmentNavigationManager.popBackStack
         */
        fun tag(tag: String?): Builder<T> = apply { this.tag = tag }

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        fun customAnimations(@AnimatorRes @AnimRes enter: Int,
                             @AnimatorRes @AnimRes exit: Int): Builder<T> = apply {
            this.enterAnimRes = enter
            this.exitAnimRes = exit
        }

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        fun customAnimations(@AnimatorRes @AnimRes enter: Int,
                             @AnimatorRes @AnimRes exit: Int,
                             @AnimatorRes @AnimRes popEnter: Int,
                             @AnimatorRes @AnimRes popExit: Int): Builder<T> = apply {
            this.enterAnimRes = enter
            this.exitAnimRes = exit
            this.popEnterAnimRes = popEnter
            this.popExitAnimRes = popExit
        }

        /**
         * Call multiple times to add several shared elements.
         *
         * @see FragmentTransaction.addSharedElement
         */
        fun sharedElement(sharedElement: View, name: String): Builder<T> = apply {
            this.sharedElementSourceView.add(sharedElement)
            this.sharedElementTargetNames.add(name)
        }

        /**
         * @see FragmentTransaction.setTransition
         */
        fun transition(transit: Int): Builder<T> = apply { this.transition = transit }

        /**
         * @see FragmentTransaction.setTransitionStyle
         */
        fun transitionStyle(@StyleRes styleRes: Int): Builder<T> =
            apply { this.transitionStyle = styleRes }

        /**
         * @see FragmentTransaction.setBreadCrumbTitle
         */
        fun breadCrumbTitle(@StringRes res: Int): Builder<T> =
            apply { this.breadCrumbShortTitleRes = res }

        /**
         * @see FragmentTransaction.setBreadCrumbTitle
         */
        fun breadCrumbTitle(text: CharSequence?): Builder<T> = apply { this.breadCrumbTitle = text }

        /**
         * @see FragmentTransaction.setBreadCrumbShortTitle
         */
        fun breadCrumbShortTitle(@StringRes res: Int): Builder<T> =
            apply { this.breadCrumbShortTitleRes = res }

        /**
         * @see FragmentTransaction.setBreadCrumbShortTitle
         */
        fun breadCrumbShortTitle(text: CharSequence?): Builder<T> =
            apply { this.breadCrumbShortTitle = text }

        fun build() = FragmentNavigationRequest(this)
    }
}

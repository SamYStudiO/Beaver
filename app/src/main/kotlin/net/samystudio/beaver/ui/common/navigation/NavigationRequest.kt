@file:Suppress("MemberVisibilityCanBePrivate", "unused", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.common.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.*
import android.support.annotation.IntRange
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseSimpleFragment

sealed class NavigationRequest
{
    /**
     * Advanced [android.app.Activity] request to use along with [ActivityNavigationManager.startActivity].
     */
    class ActivityRequest
    private constructor(private val activityClass: Class<out BaseActivity<*>>? = null,
                        private val intent: Intent? = null, val extras: Bundle? = null,
                        val options: Bundle? = null, val forResultRequestCode: Int? = null,
                        val finishCurrentActivity: Boolean = false) : NavigationRequest()
    {
        constructor(activityClass: Class<out BaseActivity<*>>, extras: Bundle? = null,
                    options: Bundle? = null, forResultRequestCode: Int? = null,
                    finishCurrentActivity: Boolean = false) :
                this(activityClass, null, extras, options, forResultRequestCode,
                     finishCurrentActivity)

        constructor(intent: Intent, options: Bundle? = null, forResultRequestCode: Int? = null,
                    finishCurrentActivity: Boolean = false) :
                this(null, intent, null, options, forResultRequestCode, finishCurrentActivity)

        fun intent(context: Context) = intent ?: Intent(context, activityClass)
    }

    /**
     * Advanced [Fragment] request to use along with [FragmentNavigationManager.startFragment].
     */
    class FragmentRequest<T : BaseSimpleFragment>(
        val fragment: T,

        /**
         * Get [Bundle] that will be inject (or added if one already exists) to [BaseSimpleFragment]
         * when request is executed.
         *
         * @see BaseSimpleFragment.setArguments
         */
        var bundle: Bundle? = null,

        /**
         * Get if this request will be added to back stack when request is executed.
         */
        var addToBackStack: Boolean = false,

        var forResultRequestCode: Int? = null,

        /**
         * Tag use for different purposes (see links below).
         *
         * @see android.support.v4.app.FragmentManager.findFragmentByTag
         * @see FragmentNavigationManager.popBackStack
         */
        var tag: String? = null,

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        @param:AnimatorRes @field:AnimatorRes
        @param:AnimRes @field:AnimRes
        var enterAnimRes: Int = 0,

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        @param:AnimatorRes @field:AnimatorRes
        @param:AnimRes @field:AnimRes
        var exitAnimRes: Int = 0,

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        @param:AnimatorRes @field:AnimatorRes
        @param:AnimRes @field:AnimRes
        var popEnterAnimRes: Int = 0,

        /**
         * @see FragmentTransaction.setCustomAnimations
         */
        @param:AnimatorRes @field:AnimatorRes
        @param:AnimRes @field:AnimRes
        var popExitAnimRes: Int = 0,

        /**
         * @see FragmentTransaction.addSharedElement
         */
        var sharedElementSourceViews: MutableList<View> = ArrayList(),

        /**
         * @see FragmentTransaction.addSharedElement
         */
        var sharedElementTargetNames: MutableList<String> = ArrayList(),

        /**
         * @see FragmentTransaction.setTransition
         */
        var transition: Int = 0,

        /**
         * @see FragmentTransaction.setTransitionStyle
         */
        @param:StyleRes @field:StyleRes
        var transitionStyle: Int = 0,

        /**
         * @see FragmentTransaction.setBreadCrumbTitle
         */
        @param:StringRes @field:StringRes
        var breadCrumbTitleRes: Int = 0,
        var breadCrumbTitle: CharSequence? = null,

        /**
         * @see FragmentTransaction.setBreadCrumbShortTitle
         */
        @param:StringRes @field:StringRes
        var breadCrumbShortTitleRes: Int = 0,
        var breadCrumbShortTitle: CharSequence? = null) : NavigationRequest()
    {
        @Suppress("UNCHECKED_CAST")
        constructor(context: Context,
                    fragmentClass: Class<T>,
                    bundle: Bundle? = null,
                    addToBackStack: Boolean = false,
                    forResultRequestCode: Int? = null,
                    tag: String? = null,
                    @AnimatorRes @AnimRes
                    enterAnimRes: Int = 0,
                    @AnimatorRes @AnimRes
                    exitAnimRes: Int = 0,
                    @AnimatorRes @AnimRes
                    popEnterAnimRes: Int = 0,
                    @AnimatorRes @AnimRes
                    popExitAnimRes: Int = 0,
                    sharedElementSourceViews: MutableList<View> = ArrayList(),
                    sharedElementTargetNames: MutableList<String> = ArrayList(),
                    transition: Int = 0,
                    @StyleRes
                    transitionStyle: Int = 0,
                    @StringRes
                    breadCrumbTitleRes: Int = 0,
                    breadCrumbTitle: CharSequence? = null,
                    @StringRes
                    breadCrumbShortTitleRes: Int = 0,
                    breadCrumbShortTitle: CharSequence? = null) : this(
            Fragment.instantiate(context, fragmentClass.name) as T, bundle, addToBackStack,
            forResultRequestCode, tag, enterAnimRes, exitAnimRes, popEnterAnimRes, popExitAnimRes,
            sharedElementSourceViews, sharedElementTargetNames, transition, transitionStyle,
            breadCrumbTitleRes, breadCrumbTitle, breadCrumbShortTitleRes, breadCrumbShortTitle)

        init
        {
            if (sharedElementSourceViews.size != sharedElementTargetNames.size)
                throw IllegalArgumentException(
                    "sharedElementSourceViews and sharedElementTargetNames array size must match")
        }

        fun addShareElement(sourceView: View,
                            targetName: String)
        {
            sharedElementSourceViews.add(sourceView)
            sharedElementTargetNames.add(targetName)
        }

        fun removeShareElement(sourceView: View)
        {
            sharedElementTargetNames.removeAt(sharedElementSourceViews.indexOf(sourceView))
            sharedElementSourceViews.remove(sourceView)
        }

        fun removeShareElement(targetName: String)
        {
            sharedElementSourceViews.removeAt(sharedElementTargetNames.indexOf(targetName))
            sharedElementTargetNames.remove(targetName)
        }

        fun prepareTransaction(transaction: FragmentTransaction): FragmentTransaction
        {
            transaction
                .setCustomAnimations(enterAnimRes,
                                     exitAnimRes,
                                     popEnterAnimRes,
                                     popExitAnimRes)
                .setTransition(transition)
                .setTransitionStyle(transitionStyle)

            if (breadCrumbTitleRes > 0)
                transaction.setBreadCrumbTitle(breadCrumbTitleRes)
            else transaction.setBreadCrumbTitle(breadCrumbTitle)

            if (breadCrumbTitleRes > breadCrumbShortTitleRes)
                transaction.setBreadCrumbShortTitle(breadCrumbShortTitleRes)
            else transaction.setBreadCrumbShortTitle(breadCrumbShortTitle)

            sharedElementSourceViews.forEachIndexed { index, view ->
                transaction.addSharedElement(view, sharedElementTargetNames[index])
            }

            bundle?.let {
                if (this.fragment.arguments != null)
                    this.fragment.arguments?.putAll(it)
                else
                    this.fragment.arguments = it
            }

            val tag: String = if (tag.isNullOrEmpty()) fragment.javaClass.name else tag!!

            if (addToBackStack)
                transaction.addToBackStack(tag)

            return transaction
        }
    }

    class ClearStackRequest : NavigationRequest()

    class BackStackRequest
    private constructor(val tag: String? = null,
                        val flags: Int = 0,
                        @param:IntRange(from = 1) @field:IntRange(from = 1)
                        val offset: Int? = null) : NavigationRequest()
    {
        constructor() : this(null, 0, null)
        constructor(tag: String, flags: Int) : this(tag, flags, null)
        constructor(@IntRange(from = 1) offset: Int? = null) : this(null, 0, offset)
    }

    class UrlRequest(val uri: Uri) : NavigationRequest()
    {
        constructor(url: String) : this(Uri.parse(url))
    }
}
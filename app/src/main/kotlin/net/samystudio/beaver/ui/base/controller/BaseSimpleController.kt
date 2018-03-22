@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseSimpleController : LifecycleController()
{
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected open lateinit var firebaseAnalytics: FirebaseAnalytics
    private var unBinder: Unbinder? = null
    @State
    protected var resultCode: Int = Activity.RESULT_CANCELED
    @State
    protected var resultIntent: Intent? = null
    @State
    protected var targetRequestCode: Int = 0

    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)

        StateSaver.restoreInstanceState(this, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        val view = inflater.inflate(layoutViewRes, container, false)
        unBinder = ButterKnife.bind(this, view)
        onViewCreated(view)

        return view
    }

    override fun onContextAvailable(context: Context)
    {
        super.onContextAvailable(context)

        if (!::firebaseAnalytics.isInitialized)
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    open fun onViewCreated(view: View)
    {
    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)

        firebaseAnalytics.setCurrentScreen(activity!!, javaClass.simpleName, javaClass.simpleName)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        StateSaver.saveInstanceState(this, outState)
    }

    override fun onDestroyView(view: View)
    {
        super.onDestroyView(view)

        unBinder?.unbind()
        unBinder = null
    }

    fun setTargetController(controller: Controller, requestCode: Int)
    {
        targetController = controller
        targetRequestCode = requestCode
    }

    /**
     * @see [android.app.Activity.setResult]
     */
    fun setResult(code: Int, intent: Intent?)
    {
        resultCode = code
        resultIntent = intent
    }

    open fun finish()
    {
        targetController?.onActivityResult(targetRequestCode, resultCode, resultIntent)

        router.popController(this)
    }
}
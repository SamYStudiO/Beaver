package net.samystudio.beaver.ui.base.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

abstract class BaseActionBarActivity<VM : BaseActivityViewModel> : BaseActivity<VM>()
{
    protected abstract val toolbar: Toolbar

    /**
     * Optional toolbar title view if action bar provided view can't match our needs. This view
     * will fade in/out when text change.
     */
    protected abstract val toolbarTitle: TextView?

    /**
     * Change title and try to fade in/out if possible.
     *
     * @see setTitle
     */
    var animatedTitle: CharSequence?
        get() = title
        set(value)
        {
            title = value
            toolbarTitle?.let {

                if (it.text == title) return

                it.animate().cancel()

                if (title.isNullOrBlank())
                {
                    hideTitle()?.setListener(object : AnimatorListenerAdapter()
                                             {
                                                 override fun onAnimationEnd(animation: Animator?)
                                                 {
                                                     it.text = ""
                                                 }
                                             })
                }
                else
                {
                    if (it.text.isNullOrBlank())
                    {
                        it.text = title
                        showTitle()
                    }
                    else
                    {
                        hideTitle()?.setListener(object : AnimatorListenerAdapter()
                                                 {
                                                     override fun onAnimationEnd(animation: Animator?)
                                                     {
                                                         it.text = title
                                                         showTitle()
                                                     }
                                                 })
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
    }

    private fun showTitle() =
        toolbarTitle
            ?.animate()
            ?.alpha(1.0f)

    private fun hideTitle() =
        toolbarTitle
            ?.animate()
            ?.alpha(0.0f)
}

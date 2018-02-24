package net.samystudio.beaver.ui.base.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.ui.base.fragment.BaseFragment

abstract class BaseActionBarActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener
{
    override fun init(savedInstanceState: Bundle?)
    {
        setSupportActionBar(getToolBar())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home)
        {
            val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()
            val count = supportFragmentManager.backStackEntryCount

            if ((currentFragment == null || !currentFragment.onOptionsItemSelected(item)) && count > 0)
                return fragmentNavigationManager.popBackStack()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackStackChanged()
    {
        super.onBackStackChanged()

        if (getToolBarTitle() != null)
        {
            val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()

            currentFragment
                ?.titleObservable
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ title -> toggleTitle(title) })
        }
    }

    private fun toggleTitle(title: String?)
    {
        val view = getToolBarTitle()

        if (view != null)
        {
            if (view.text == title) return

            view.animate().cancel()

            if (title.isNullOrBlank())
            {
                hideTitle()?.setListener(object : AnimatorListenerAdapter()
                                         {
                                             override fun onAnimationEnd(
                                                     animation: Animator?)
                                             {
                                                 view.text = ""
                                             }
                                         })
            }
            else
            {
                if (view.text.isNullOrBlank())
                {
                    view.text = title
                    showTitle()
                }
                else
                {
                    hideTitle()?.setListener(object : AnimatorListenerAdapter()
                                             {
                                                 override fun onAnimationEnd(animation: Animator?)
                                                 {
                                                     view.text = title
                                                     showTitle()
                                                 }
                                             })
                }
            }
        }
        else getToolBar().title = title
    }

    private fun showTitle() =
            getToolBarTitle()
                    ?.animate()
                    ?.alpha(1.0f)

    private fun hideTitle() =
            getToolBarTitle()
                    ?.animate()
                    ?.alpha(0.0f)

    abstract fun getToolBar(): Toolbar

    /**
     * Optional toolbar title view if action bar provided view can't match our needs. This view
     * will fade in/out when text change.
     */
    abstract fun getToolBarTitle(): TextView?
}

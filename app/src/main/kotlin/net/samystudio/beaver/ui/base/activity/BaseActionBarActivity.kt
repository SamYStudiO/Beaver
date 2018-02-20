package net.samystudio.beaver.ui.base.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
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
            val actual = getToolBarTitle()?.text

            currentFragment
                    ?.getTitleObservable()
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ title ->
                                    if (actual != title)
                                    {
                                        if (title.isNullOrBlank()) showTitle(title)
                                        else hideTitle()
                                    }
                                })
        }
    }

    private fun showTitle(text: String?)
    {
        val title = getToolBarTitle()

        title?.text = text
        title?.animate()?.alpha(1.0f)?.setListener(null)
    }

    private fun hideTitle()
    {
        val title = getToolBarTitle()
        title
                ?.animate()
                ?.alpha(0.0f)
                ?.setListener(object : AnimatorListenerAdapter()
                              {
                                  override fun onAnimationEnd(animation: Animator)
                                  {
                                      title.text = ""
                                      title.visibility = View.GONE
                                  }
                              })
    }

    abstract fun getToolBar(): Toolbar
    abstract fun getToolBarTitle(): TextView?
}

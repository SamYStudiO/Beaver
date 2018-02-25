@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evernote.android.state.State
import dagger.android.support.DaggerFragment
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.processors.BehaviorProcessor
import net.samystudio.beaver.ui.base.dialog.DialogResult
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import net.samystudio.beaver.ui.common.navigation.NavigationManager
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment(), HasSupportFragmentInjector
{
    @Inject
    protected lateinit var navigationManager: NavigationManager
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
    @get:LayoutRes
    protected abstract val layoutViewRes: Int

    private val _titleObservable: BehaviorProcessor<String> = BehaviorProcessor.create()
    val titleObservable: Observable<String> = _titleObservable.toObservable()
    @State
    var title: String = ""
        set(value)
        {
            field = value
            _titleObservable.onNext(value)
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        _titleObservable.onNext(title)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return if (layoutViewRes > 0) inflater.inflate(layoutViewRes, container, false)
        else null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        init(savedInstanceState)
    }

    override fun onDestroy()
    {
        _titleObservable.onComplete()

        super.onDestroy()
    }

    /**
     * Override this to catch back key pressed.
     *
     * @return true if you consume event, this means no more parent will catch this event, false
     * if you want parent to handle this event.
     */
    fun onBackPressed() = false

    fun onDialogResult(requestCode: Int, result: DialogResult)
    {
    }

    protected abstract fun init(savedInstanceState: Bundle?)
}

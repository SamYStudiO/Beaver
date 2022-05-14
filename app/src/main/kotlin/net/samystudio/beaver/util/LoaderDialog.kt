package net.samystudio.beaver.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.common.dialog.LoaderDialog

/**
 * Loader dialog that prevent user interaction while loading. Mostly used when posting data to
 * server.
 */
fun Fragment.showLoaderDialog() {
    val view = this.view ?: return

    /* We don't use navigation component here because navigating with navigation component is
    async and so if loading is really short and we immediately call hideLoaderDialog then dialog
    may never be closed. */
    if (view.getTag(R.id.tag_loader_dialog_id) !is LifecycleObserver) {
        LoaderDialog().showNow(
            childFragmentManager,
            LoaderDialog::class.simpleName + javaClass.simpleName
        )

        lifecycle.addObserver(
            object :
                DefaultLifecycleObserver {
                val viewLifecycleOwnerLiveDataObserver =
                    Observer<LifecycleOwner?> {
                        val viewLifecycleOwner = it ?: return@Observer

                        viewLifecycleOwner.lifecycle.addObserver(object :
                                DefaultLifecycleObserver {
                                override fun onDestroy(owner: LifecycleOwner) {
                                    hideLoaderDialog()
                                    viewLifecycleOwner.lifecycle.removeObserver(this)
                                }
                            })
                    }

                override fun onCreate(owner: LifecycleOwner) {
                    viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
                }
            }.also { view.setTag(R.id.tag_loader_dialog_id, it) }
        )
    }
}

fun Fragment.hideLoaderDialog() {
    (
        childFragmentManager.findFragmentByTag(
            LoaderDialog::class.simpleName + javaClass.simpleName
        ) as? LoaderDialog
        )
        ?.dismissAllowingStateLoss()

    (view?.getTag(R.id.tag_loader_dialog_id) as? LifecycleObserver)?.let {
        lifecycle.removeObserver(it)
        view?.setTag(R.id.tag_loader_dialog_id, null)
    }
}

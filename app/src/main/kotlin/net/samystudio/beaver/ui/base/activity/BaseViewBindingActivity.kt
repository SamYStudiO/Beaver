package net.samystudio.beaver.ui.base.activity

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

abstract class BaseViewBindingActivity<VB : ViewBinding, VM : BaseActivityViewModel> :
    BaseActivity<VM>() {
    protected abstract val binding: VB

    inline fun <reified T : ViewBinding> viewBinding(crossinline inflate: (inflater: LayoutInflater) -> T): Lazy<T> =
        object : Lazy<T> {
            private var binding: T? = null
            override fun isInitialized(): Boolean = binding != null
            override val value: T
                get() = binding ?: inflate(window.layoutInflater).also {
                    binding = it
                    setContentView(it.root)
                }
        }
}
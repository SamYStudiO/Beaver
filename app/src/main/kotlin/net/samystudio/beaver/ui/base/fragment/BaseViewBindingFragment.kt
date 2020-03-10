package net.samystudio.beaver.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingFragment<VB : ViewBinding> : BaseFragment() {
    protected abstract val binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    inline fun <reified T : ViewBinding> viewBinding(crossinline inflate: (inflater: LayoutInflater, container: ViewGroup?) -> T): Lazy<T> =
        object : Lazy<T> {
            private var binding: T? = null
            override fun isInitialized(): Boolean = binding != null
            override val value: T
                get() = binding ?: inflate(
                    layoutInflater,
                    requireActivity().findViewById(id) as? ViewGroup
                ).also {
                    binding = it
                    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                        @Suppress("unused")
                        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                        fun onDestroyView() {
                            viewLifecycleOwner.lifecycle.removeObserver(this)
                            binding = null
                        }
                    })
                }
        }


    inline fun <reified T : ViewBinding> viewBinding(crossinline bind: (view: View) -> T): Lazy<T> =
        object : Lazy<T> {
            private var binding: T? = null
            override fun isInitialized(): Boolean = binding != null
            override val value: T
                get() = binding ?: bind(
                    requireDialog().window!!.decorView
                ).also {
                    binding = it
                    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                        @Suppress("unused")
                        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                        fun onDestroyView() {
                            viewLifecycleOwner.lifecycle.removeObserver(this)
                            binding = null
                        }
                    })
                }
        }
}
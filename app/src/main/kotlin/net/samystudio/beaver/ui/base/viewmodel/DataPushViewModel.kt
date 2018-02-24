@file:Suppress("unused")

package net.samystudio.beaver.ui.base.viewmodel

interface DataPushViewModel
{
    fun onPushSuccess()
    fun onPushError(throwable: Throwable? = null) = throwable?.printStackTrace()
}
package net.samystudio.beaver.ui.common.viewmodel

import android.support.annotation.MainThread

open class TriggerCommand : Command<Nothing>()
{
    @MainThread
    fun call()
    {
        value = null
    }
}
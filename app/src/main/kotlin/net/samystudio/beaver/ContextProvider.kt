package net.samystudio.beaver

import android.content.Context
import androidx.startup.Initializer

class ContextProvider : Initializer<Unit> {
    override fun create(context: Context) {
        applicationContext = context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    companion object {
        lateinit var applicationContext: Context
    }
}

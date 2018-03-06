package net.samystudio.beaver.ext

import android.app.Activity
import android.content.ContextWrapper
import android.view.View

/**
 * Get [Activity] from a [View], may return null depending of [View] state.
 */
val View.activity: Activity?
    get()
    {
        var context = context

        while (context is ContextWrapper)
        {
            if (context is Activity)
                return context

            context = context.baseContext
        }

        return null
    }
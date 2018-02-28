package net.samystudio.beaver.ext

import android.app.Activity
import android.content.ContextWrapper
import android.view.View

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
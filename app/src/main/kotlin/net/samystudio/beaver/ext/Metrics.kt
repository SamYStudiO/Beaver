@file:Suppress("unused")

package net.samystudio.beaver.ext

import android.content.res.Resources
import android.util.TypedValue

fun dp2px(resources: Resources, dp: Float) =
    Math.round(TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics))

fun sp2pt(resources: Resources, sp: Float) =
    Math.round(TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics))
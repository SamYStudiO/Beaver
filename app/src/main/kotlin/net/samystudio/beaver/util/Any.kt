@file:Suppress("unused")

package net.samystudio.beaver.util

fun Any.getClassTag(): String = this.javaClass.name
fun Any.getClassSimpleTag(): String = this.javaClass.simpleName

@Suppress("NOTHING_TO_INLINE")
inline fun Any.getMethodTag(): String =
    getClassTag() + "::" + object : Any() {}.javaClass.enclosingMethod?.name

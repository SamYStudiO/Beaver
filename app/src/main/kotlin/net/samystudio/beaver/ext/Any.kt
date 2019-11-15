package net.samystudio.beaver.ext

fun Any.getClassTag(): String = this.javaClass.name

@Suppress("NOTHING_TO_INLINE")
inline fun Any.getMethodTag(): String =
    getClassTag() + "::" + object : Any() {}.javaClass.enclosingMethod?.name

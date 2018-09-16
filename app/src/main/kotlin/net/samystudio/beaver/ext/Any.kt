package net.samystudio.beaver.ext

fun Any.getTag(): String = this.javaClass.simpleName

fun Any.getMethodTag(): String =
    getTag() + object : Any() {}.javaClass.enclosingMethod?.name

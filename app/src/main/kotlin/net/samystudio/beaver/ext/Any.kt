package net.samystudio.beaver.ext

fun Any.getMethodTag(): String =
    this.javaClass.simpleName + object : Any() {}.javaClass.enclosingMethod?.name

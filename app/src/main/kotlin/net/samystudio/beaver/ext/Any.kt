package net.samystudio.beaver.ext

fun Any.getClassTag(): String = this.javaClass.name

fun Any.getMethodTag(): String =
    getClassTag() + object : Any() {}.javaClass.enclosingMethod?.name

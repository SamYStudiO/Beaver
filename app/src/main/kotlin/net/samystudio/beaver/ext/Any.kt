package net.samystudio.beaver.ext

fun Any.getMethodTag(): String = this.javaClass.simpleName + this.javaClass.enclosingMethod?.name

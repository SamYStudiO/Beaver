package net.samystudio.beaver.ui.main.home

sealed class HomeUserFlow {
    object Disconnect : HomeUserFlow()
}
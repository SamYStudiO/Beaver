package net.samystudio.beaver.ui.main.userProfile

sealed class UserProfileUserFlow {
    object Disconnect : UserProfileUserFlow()
}
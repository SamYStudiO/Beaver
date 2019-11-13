package net.samystudio.beaver.di.module

import dagger.Module
import dagger.Provides
import net.samystudio.beaver.data.model.User

@Module
abstract class UserModule(private val user: User) {
    @Provides
    fun provideUser(): User = user
}
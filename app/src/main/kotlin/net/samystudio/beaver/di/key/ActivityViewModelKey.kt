package net.samystudio.beaver.di.key

import dagger.MapKey
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
@MustBeDocumented
annotation class ActivityViewModelKey(val value: KClass<out BaseActivityViewModel>)
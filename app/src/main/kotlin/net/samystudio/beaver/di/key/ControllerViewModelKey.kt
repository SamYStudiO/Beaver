package net.samystudio.beaver.di.key

import dagger.MapKey
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
@MustBeDocumented
annotation class ControllerViewModelKey(val value: KClass<out BaseControllerViewModel>)
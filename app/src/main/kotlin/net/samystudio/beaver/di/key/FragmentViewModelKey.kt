package net.samystudio.beaver.di.key

import dagger.MapKey
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
@MustBeDocumented
annotation class FragmentViewModelKey(val value: KClass<out BaseFragmentViewModel>)
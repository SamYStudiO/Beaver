package net.samystudio.beaver.ui.common.model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import net.samystudio.beaver.di.scope.FragmentScope

import javax.inject.Inject
import javax.inject.Provider

@FragmentScope
class ViewModelFactory
@Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
        ViewModelProvider.Factory
{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        var creator: Provider<out ViewModel>? = creators[modelClass]

        if (creator == null)
        {
            for ((key, value) in creators)
            {
                if (modelClass.isAssignableFrom(key))
                {
                    creator = value
                    break
                }
            }
        }

        if (creator == null) throw IllegalArgumentException("unknown model class " + modelClass)

        try
        {
            return creator.get() as T
        }
        catch (e: Exception)
        {
            throw RuntimeException(e)
        }
    }
}

package net.samystudio.beaver.ui.main.home

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import timber.log.Timber


class HomeAssistedInject @AssistedInject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    @Assisted private val toto: Int
) {

    fun test() {
        Timber.d("test1: %s", sharedPreferencesHelper)
        Timber.d("test2: %s", toto)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(toto: Int): HomeAssistedInject
    }
}
package net.samystudio.beaver.ui.common.dialog

import android.accounts.AccountManager
import android.app.Application
import net.samystudio.beaver.data.local.SharedPreferencesManager
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.main.MainActivityViewModel
import javax.inject.Inject

@FragmentScope
class AlertDialogViewModel
@Inject
constructor(application: Application,
            accountManager: AccountManager,
            sharedPreferencesManager: SharedPreferencesManager,
            activityViewModel: MainActivityViewModel) :
    BaseFragmentViewModel(application, accountManager, sharedPreferencesManager, activityViewModel)
{
    override val defaultTitle: String?
        get() = "hello"
}
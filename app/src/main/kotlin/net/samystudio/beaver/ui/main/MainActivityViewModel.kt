package net.samystudio.beaver.ui.main

import android.accounts.AccountManager
import android.app.Application
import net.samystudio.beaver.data.local.SharedPreferencesManager
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel
@Inject
constructor(application: Application,
            accountManager: AccountManager,
            sharedPreferencesManager: SharedPreferencesManager) :
    BaseActivityViewModel(application, accountManager, sharedPreferencesManager)
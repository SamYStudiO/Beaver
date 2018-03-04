package net.samystudio.beaver.ui.base.viewmodel

import android.accounts.AccountManager
import android.app.Application
import net.samystudio.beaver.data.local.SharedPreferencesManager

abstract class BaseActivityViewModel
constructor(application: Application,
            accountManager: AccountManager,
            sharedPreferencesManager: SharedPreferencesManager) :
    BaseViewControllerViewModel(application, accountManager, sharedPreferencesManager)
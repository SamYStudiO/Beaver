@file:Suppress("ProtectedInFinal")

package net.samystudio.beaver.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.data.manager.AuthenticatorManager
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticatorService : Service() {
    @Inject
    protected lateinit var authenticatorManager: AuthenticatorManager

    override fun onBind(intent: Intent): IBinder = authenticatorManager.iBinder
}
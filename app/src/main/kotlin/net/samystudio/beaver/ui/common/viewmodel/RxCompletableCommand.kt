@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import net.samystudio.beaver.data.remote.CommandRequestState

class RxCompletableCommand : Command<CommandRequestState>(), Disposable
{
    private var disposable: Disposable? = null

    fun execute(completable: Completable)
    {
        if (disposable != null && !disposable!!.isDisposed)
            throw IllegalStateException("Cannot execute a command while another is in progress")

        disposable = completable
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { value = CommandRequestState.Start() }
            .subscribe({ value = CommandRequestState.Complete() },
                       { throwable -> value = CommandRequestState.Error(throwable) })
    }

    override fun dispose()
    {
        disposable?.dispose()
    }

    // Just to be Disposable compliant, don't care about that value.
    override fun isDisposed(): Boolean = false
}
package net.samystudio.beaver.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import net.samystudio.beaver.ui.common.dialog.AlertDialog
import net.samystudio.beaver.ui.common.dialog.setDialogPositiveClickListener

/**
 * Make sure we are in the right destination before navigating to other destination.
 * This is useful for example when receiving data from [AlertDialog] with
 * [setDialogPositiveClickListener] for example and dialog has not be dismissed yet and so current
 * destination id is still not fragment destination id where you may want to navigate elsewhere.
 */
fun Fragment.navigateWhenCurrentDestinationIdIs(destinationId: Int, readyToNavigate: () -> Unit) {
    val listener = object :
        NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?
        ) {
            if (destination.id == destinationId) {
                readyToNavigate.invoke()
                findNavController().removeOnDestinationChangedListener(this)
            }
        }
    }

    findNavController().addOnDestinationChangedListener(listener)

    viewLifecycleOwnerLiveData.observe(this@navigateWhenCurrentDestinationIdIs) {
        it.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                findNavController().removeOnDestinationChangedListener(listener)
            }
        })
    }
}

/**
 * Make sure we are in the right destination to navigate to other destination.
 * This is useful for example when you open a dialog and try to navigate from fragment that opened
 * the dialog, you'll get a crash since current destination is not the right one.
 */
fun Fragment.navigateIfCurrentDestinationIdIs(destinationId: Int, readyToNavigate: () -> Unit) {
    if (findNavController().currentDestination?.id == destinationId) readyToNavigate.invoke()
}

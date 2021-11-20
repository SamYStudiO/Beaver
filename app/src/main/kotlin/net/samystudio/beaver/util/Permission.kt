@file:Suppress("unused")

package net.samystudio.beaver.util

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.random.Random

fun Activity.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    requireContext(),
    permission
) == PackageManager.PERMISSION_GRANTED

fun Activity.hasPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

fun Fragment.hasPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

/**
 * @see [RequestPermissionLauncher]
 */
fun Fragment.createPermissionLauncher(
    permission: String,
    maxSdkInt: Int? = null,
    rationale: (() -> Unit)? = null,
    denied: (() -> Unit)? = null,
    success: (() -> Unit)? = null,
) = RequestPermissionLauncher(
    permission,
    this,
    rationale,
    denied,
    success,
    maxSdkInt
)

class RequestPermissionLauncher(
    private val permission: String,
    private val fragment: Fragment,
    /**
     * A optional rationale callback called everytime this launcher is launched and a rationale
     * should be present to user.
     */
    private val globalRationale: (() -> Unit)? = null,
    /**
     * A optional denied callback called everytime this launcher is launched and failed.
     */
    private val globalDenied: (() -> Unit)? = null,
    /**
     * A optional success callback called everytime this launcher is launched and succeeded.
     */
    private val globalSuccess: (() -> Unit)? = null,
    private val maxSdkInt: Int? = null
) {
    private val codeRational = Random.nextInt()
    private val codeNeverAskAgain = Random.nextInt()
    private var localRationale: (() -> Unit)? = null
    private var localDenied: (() -> Unit)? = null
    private var localSuccess: (() -> Unit)? = null
    private val launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                globalSuccess?.invoke()
                localSuccess?.invoke()
            } else {
                globalDenied?.invoke()
                localDenied?.invoke()
            }
        }

    /**
     * @param rationale A optional rationale callback called for this specific launch when this
     * launcher is launched and a rationale should be present to user.
     * in addition to [globalDenied] callback.
     * @param denied A optional denied callback called for this specific launch when this launcher
     * failed in addition to [globalDenied] callback.
     * @param success A optional success callback called for this specific launch when this launcher
     * succeeded in addition to [globalSuccess] callback.
     */
    fun launch(
        rationale: (() -> Unit)? = null,
        denied: (() -> Unit)? = null,
        success: (() -> Unit)? = null,
    ) {
        this.localRationale = rationale
        this.localDenied = denied
        this.localSuccess = success

        when {
            fragment.hasPermission(permission) ||
                    (maxSdkInt != null && Build.VERSION.SDK_INT > maxSdkInt) -> {
                globalSuccess?.invoke()
                localSuccess?.invoke()
            }
            fragment.shouldShowRequestPermissionRationale(permission) -> {
                globalRationale?.invoke()
                localRationale?.invoke()
            }
            else -> launcher.launch(permission)
        }
    }
}

/**
 * @see [RequestPermissionsLauncher]
 */
fun Fragment.createPermissionsLauncher(
    vararg permissions: String,
    rationale: ((permissions: Set<String>) -> Unit)? = null,
    denied: ((permissions: Set<String>) -> Unit)? = null,
    success: (() -> Unit)? = null,
) = RequestPermissionsLauncher(
    permissions.toSet(),
    this,
    rationale,
    denied,
    success,
)

class RequestPermissionsLauncher(
    private val permissions: Set<String>,
    private val fragment: Fragment,
    /**
     * A optional rationale callback called everytime this launcher is launched and a rationale
     * should be present to user.
     */
    private val globalRationale: ((permissions: Set<String>) -> Unit)? = null,
    /**
     * A optional denied callback called everytime this launcher is launched and failed.
     */
    private val globalDenied: ((permissions: Set<String>) -> Unit)? = null,
    /**
     * A optional success callback called everytime this launcher is launched and succeeded.
     */
    private val globalSuccess: (() -> Unit)? = null,
) {
    private val codeRational = Random.nextInt()
    private val codeNeverAskAgain = Random.nextInt()
    private var localRationale: ((permissions: Set<String>) -> Unit)? = null
    private var localDenied: ((permissions: Set<String>) -> Unit)? = null
    private var localSuccess: (() -> Unit)? = null
    private val launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.filter { !it.value }.isEmpty()) {
                globalSuccess?.invoke()
                localSuccess?.invoke()

            } else
                map.filter { !it.value }.map { it.key }.toSet().let {
                    if (it.isNotEmpty()) {
                        globalDenied?.invoke(it)
                        localDenied?.invoke(it)
                    }
                }
        }

    /**
     * @param rationale A optional rationale callback called for this specific launch when this
     * launcher is launched and a rationale should be present to user.
     * in addition to [globalDenied] callback.
     * @param denied A optional denied callback called for this specific launch when this launcher
     * failed in addition to [globalDenied] callback.
     * @param success A optional success callback called for this specific launch when this launcher
     * succeeded in addition to [globalSuccess] callback.
     */
    fun launch(
        rationale: ((permissions: Set<String>) -> Unit)? = null,
        denied: ((permissions: Set<String>) -> Unit)? = null,
        success: (() -> Unit)? = null,
    ) {
        this.localRationale = rationale
        this.localDenied = denied
        this.localSuccess = success

        val rationales =
            permissions.filter { fragment.shouldShowRequestPermissionRationale(it) }.toSet()

        when {
            fragment.hasPermissions(*permissions.toTypedArray()) -> {
                globalSuccess?.invoke()
                localSuccess?.invoke()
            }
            rationales.isNotEmpty() -> {
                globalRationale?.invoke(rationales)
                localRationale?.invoke(rationales)
            }
            else -> launcher.launch(permissions.toTypedArray())
        }
    }
}

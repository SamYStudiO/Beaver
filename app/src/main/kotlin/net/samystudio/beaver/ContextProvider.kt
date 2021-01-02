package net.samystudio.beaver

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

/**
 * https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html
 */
class ContextProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        applicationContext = context!!
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ) = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0

    override fun getType(uri: Uri): String? = null

    companion object {
        lateinit var applicationContext: Context
    }
}

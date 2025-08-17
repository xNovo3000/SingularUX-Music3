package org.singularux.music.core.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

internal abstract class MusicPermissionManagerAndroid(
    private val context: Context
) : MusicPermissionManager {

    override fun hasPermission(permission: MusicPermission): Boolean {
        return ContextCompat.checkSelfPermission(context, getPermissionString(permission)) ==
                PackageManager.PERMISSION_GRANTED
    }

}
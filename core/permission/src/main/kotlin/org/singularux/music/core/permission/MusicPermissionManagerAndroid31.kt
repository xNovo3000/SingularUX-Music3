package org.singularux.music.core.permission

import android.Manifest
import android.content.Context

internal class MusicPermissionManagerAndroid31(
    context: Context
) : MusicPermissionManagerAndroid(context = context) {

    override fun getPermissionString(permission: MusicPermission): String {
        return when (permission) {
            MusicPermission.READ_MUSIC -> Manifest.permission.READ_EXTERNAL_STORAGE
            MusicPermission.READ_PHONE_STATE -> Manifest.permission.READ_PHONE_STATE
        }
    }

}
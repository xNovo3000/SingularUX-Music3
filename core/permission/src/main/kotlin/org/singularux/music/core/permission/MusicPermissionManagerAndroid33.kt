package org.singularux.music.core.permission

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MusicPermissionManagerAndroid33(
    context: Context
) : MusicPermissionManagerAndroid(context = context) {

    override fun getPermissionString(permission: MusicPermission): String {
        return when (permission) {
            MusicPermission.READ_MUSIC -> Manifest.permission.READ_MEDIA_AUDIO
            MusicPermission.READ_PHONE_STATE -> Manifest.permission.READ_PHONE_STATE
        }
    }

}
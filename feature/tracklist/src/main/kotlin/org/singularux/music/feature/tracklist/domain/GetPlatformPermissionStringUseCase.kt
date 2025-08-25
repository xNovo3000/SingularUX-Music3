package org.singularux.music.feature.tracklist.domain

import org.singularux.music.core.permission.MusicPermission
import org.singularux.music.core.permission.MusicPermissionManager
import javax.inject.Inject

class GetPlatformPermissionStringUseCase @Inject constructor(
    private val musicPermissionManager: MusicPermissionManager
) {

    operator fun invoke(permission: MusicPermission): String {
        return musicPermissionManager.getPermissionString(permission = permission)
    }

}
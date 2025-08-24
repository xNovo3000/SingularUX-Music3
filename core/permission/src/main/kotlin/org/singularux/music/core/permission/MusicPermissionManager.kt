package org.singularux.music.core.permission

interface MusicPermissionManager {

    fun hasPermission(permission: MusicPermission): Boolean
    fun getPermissionString(permission: MusicPermission): String

    fun hasPermissions(permissions: List<MusicPermission>): List<Boolean> {
        return permissions.map { hasPermission(it) }
    }

    fun getPermissionsString(permissions: List<MusicPermission>): List<String> {
        return permissions.map { getPermissionString(it) }
    }

}
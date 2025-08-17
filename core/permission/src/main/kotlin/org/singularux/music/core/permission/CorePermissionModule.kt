package org.singularux.music.core.permission

import android.content.Context
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object CorePermissionModule {

    @Provides
    @ActivityRetainedScoped
    fun providesMusicPermissionManager(
        @ApplicationContext context: Context
    ): MusicPermissionManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            MusicPermissionManagerAndroid33(context = context)
        } else {
            MusicPermissionManagerAndroid31(context = context)
        }
    }

}
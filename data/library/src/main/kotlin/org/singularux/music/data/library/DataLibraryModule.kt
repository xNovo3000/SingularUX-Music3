package org.singularux.music.data.library

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.singularux.music.core.permission.MusicPermissionManager
import org.singularux.music.data.library.repository.TrackRepository
import org.singularux.music.data.library.repository.TrackRepositoryAndroid

@Module
@InstallIn(ActivityRetainedComponent::class)
class DataLibraryModule {

    @Provides
    @ActivityRetainedScoped
    fun providesTrackRepository(
        @ApplicationContext context: Context,
        musicPermissionManager: MusicPermissionManager
    ): TrackRepository {
        return TrackRepositoryAndroid(
            context = context,
            musicPermissionManager = musicPermissionManager
        )
    }

}
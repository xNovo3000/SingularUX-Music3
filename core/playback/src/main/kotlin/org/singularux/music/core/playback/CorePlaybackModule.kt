package org.singularux.music.core.playback

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ActivityRetainedComponent::class)
object CorePlaybackModule {

    @Provides
    @ActivityRetainedScoped
    fun providesMusicControllerFacade(@ApplicationContext context: Context): MusicControllerFacade {
        return MusicControllerFacade(
            context = context,
            coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        )
    }

}
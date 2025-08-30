package org.singularux.music.feature.playback

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FeaturePlaybackSingletonModule {

    @Provides
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context = context)
    }

}
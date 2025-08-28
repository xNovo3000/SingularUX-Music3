package org.singularux.music

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp
import org.singularux.music.feature.saverestoreplaybackstate.RestorePlaybackStateWorker
import org.singularux.music.feature.saverestoreplaybackstate.SavePlaybackStateWorker
import javax.inject.Inject

@HiltAndroidApp
class MusicApplication : Application(), SingletonImageLoader.Factory, Configuration.Provider {

    companion object {
        private const val ARTWORK_MAX_SIZE_PERCENTAGE = 0.15
    }

    @Inject lateinit var hiltWorkerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context = context)
            .networkCachePolicy(policy = CachePolicy.DISABLED)
            .diskCachePolicy(policy = CachePolicy.DISABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context = context, percent = ARTWORK_MAX_SIZE_PERCENTAGE)
                    .build()
            }
            .build()
    }

}
package org.singularux.music

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApplication : Application(), SingletonImageLoader.Factory {

    companion object {
        private const val ARTWORK_MAX_SIZE_PERCENTAGE = 0.15
    }

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
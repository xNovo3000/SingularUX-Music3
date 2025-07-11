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
        private const val MAX_CACHE_SIZE = 8L * 1024 * 1024  // 8 MB
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .networkCachePolicy(CachePolicy.DISABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizeBytes(MAX_CACHE_SIZE)
                    .build()
            }
            .build()
    }

}
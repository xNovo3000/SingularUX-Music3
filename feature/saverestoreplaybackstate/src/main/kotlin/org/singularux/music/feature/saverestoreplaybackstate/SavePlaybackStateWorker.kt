package org.singularux.music.feature.saverestoreplaybackstate

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SavePlaybackStateWorker(
    appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(
    appContext = appContext,
    params = params
) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

}
package org.singularux.music.feature.playback.model

import androidx.annotation.FloatRange

data class PlaybackProgress(
    @param:FloatRange(from = 0.0, to = 1.0) val progress: Float
)
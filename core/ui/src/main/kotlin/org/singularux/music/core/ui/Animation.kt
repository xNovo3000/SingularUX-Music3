package org.singularux.music.core.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith

val MusicTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
    EnterTransition.None togetherWith ExitTransition.None
}

val MusicPopTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
    EnterTransition.None togetherWith ExitTransition.None
}

val MusicPredictivePopTransitionSpec: AnimatedContentTransitionScope<*>.(Int) -> ContentTransform =
{
    EnterTransition.None togetherWith ExitTransition.None
}
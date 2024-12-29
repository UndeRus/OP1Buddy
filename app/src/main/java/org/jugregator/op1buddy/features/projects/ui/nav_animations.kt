package org.jugregator.op1buddy.features.projects.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

val enterPush: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    fadeIn(
        animationSpec = tween(
            ANIMATION_DURATION_SHORT_MILLIS, easing = LinearEasing
        )
    ) + slideIntoContainer(
        animationSpec = tween(ANIMATION_DURATION_SHORT_MILLIS, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        initialOffset = { it }
    )
}

val exitPush: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(
        animationSpec = tween(
            ANIMATION_DURATION_MILLIS, easing = LinearEasing
        )
    ) + slideOutOfContainer(
        animationSpec = tween(ANIMATION_DURATION_MILLIS, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        targetOffset = { -it / 4 }
    )
}
val enterPop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    fadeIn(
        animationSpec = tween(
            ANIMATION_DURATION_MILLIS, easing = LinearEasing
        )
    ) + slideIntoContainer(
        animationSpec = tween(ANIMATION_DURATION_MILLIS, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        initialOffset = { -it / 4 }
    )
}
val exitPop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(
        animationSpec = tween(
            ANIMATION_DURATION_SHORT_MILLIS, easing = LinearEasing
        )
    ) + slideOutOfContainer(
        animationSpec = tween(ANIMATION_DURATION_SHORT_MILLIS, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        targetOffset = { it }
    )
}

private const val ANIMATION_DURATION_MILLIS = 300
private const val ANIMATION_DURATION_SHORT_MILLIS = 200

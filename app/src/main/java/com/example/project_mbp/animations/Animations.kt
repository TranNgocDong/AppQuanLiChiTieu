// File: com/example/project_mbp/animations/Animations.kt
package com.example.project_mbp.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween

object Animations {
    suspend fun scaleClickAnimation(scale: Animatable<Float, *>) {
        scale.animateTo(
            targetValue = 1.16f,
            animationSpec = tween(durationMillis = 100)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 150)
        )
    }
}

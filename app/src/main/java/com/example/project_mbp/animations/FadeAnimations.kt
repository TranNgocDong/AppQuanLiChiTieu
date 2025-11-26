package com.example.project_mbp.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

/**
 * Hiệu ứng tan biến (fade out) khi xóa item.
 * Gói sẵn AnimatedVisibility để dùng lại dễ dàng trong LazyColumn.
 */
@Composable
fun FadeDeleteAnimation(
    visible: Boolean,
    onAnimationFinish: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(400)),
        exit = fadeOut(animationSpec = tween(600)),
        label = "fadeDeleteAnimation"
    ) {
        content()
    }

    // Nếu visible = false thì gọi callback sau khi fadeOut hoàn tất
    if (!visible && onAnimationFinish != null) {
        LaunchedEffect(Unit) {
            delay(600)
            onAnimationFinish()
        }
    }
}

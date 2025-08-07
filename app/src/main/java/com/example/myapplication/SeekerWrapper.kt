@file:JvmName("SeekerInterop")
package com.example.myapplication

import android.content.Context
import android.os.Debug
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.sp
import dev.vivvvek.seeker.*

@Composable
fun SeekerContent(
    context: Context,
    initialValue: Float,
    initialReadAhead: Float,
    segments: List<Segment>,
    onValueChange: (Float) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDragging by interactionSource.collectIsDraggedAsState()
    var value by remember { mutableFloatStateOf(initialValue) }
    val gap by animateDpAsState(if (isDragging) 2.dp else 0.dp)
    val thumbRadius by animateDpAsState(if (isDragging) 10.dp else 8.dp)
    var readAheadValue by remember { mutableStateOf(initialReadAhead) }
    val state = rememberSeekerState()

    Seeker(
        value = value,
        readAheadValue = readAheadValue,
        range = 2f..30f,
        segments = segments,
        state = state,
        interactionSource = interactionSource,
        colors = SeekerDefaults.seekerColors(
            trackColor = Color(0xFF66BB6A), // зелёный трек
            thumbColor = Color(0xFF2E7D32),  // тёмно-зелёный кружок
            progressColor = Color(0xFF43A047), // насыщенно-зелёный (прогресс)
            readAheadColor = Color(0xFF81C784) // нежно-зелёный (отметки)
        ),
        dimensions = SeekerDefaults.seekerDimensions(
            gap = gap,
            thumbRadius = thumbRadius
        ),
        onValueChange = {
            value = it
            onValueChange(it) // сообщает наружу

        },
    )

    if (state.currentSegment != null) {
        var lastName by remember { mutableStateOf("") }
        val currentName = state.currentSegment?.name.orEmpty()

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(600)
            ),
            exit = fadeOut(animationSpec = tween(600)) + slideOutVertically(
                targetOffsetY = { -it / 2 },
                animationSpec = tween(600)
            )
        ) {
            Text(
                text = currentName,
                modifier = Modifier.padding(8.dp, 38.dp, 8.dp, 28.dp),
                color = Color(0xFFB9F6CA),
                fontSize = 18.sp,
            )
        }


        //Text(state.currentSegment!!.name)
    }
}
@JvmName("setSeekerContent")
    fun setSeekerContent(
        context : Context,
        composeView: ComposeView,
        initialValue: Float,
        initialReadAhead: Float,
        segments: List<Segment>,
        onChange: (Float) -> Unit
    ) {
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        composeView.setContent {
            var value by remember { mutableFloatStateOf(initialValue.coerceAtLeast(2f)) }
            var readAhead by remember { mutableStateOf(initialReadAhead) }
            SeekerContent(context,value, readAhead, segments, onChange)
        }
    }


@JvmName("createSegment")
fun createSegment(name: String, start: Float): Segment {
    return Segment(name = name, start = start)
}

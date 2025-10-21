package com.example.media3ex.component

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

class TopbarNestedScrollController(
    private val onChangedOffset: (Float, Float) -> Unit
): NestedScrollConnection {
    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity
    ): Velocity {
//        Log.e("vsvx13", "onPostFling consumed: $consumed available: $available")
        return super.onPostFling(consumed, available)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
//        Log.e("vsvx13", "onPostScroll consumed: $consumed available: $available source: $source")
        return super.onPostScroll(consumed, available, source)
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
//        Log.e("vsvx13", "onPreFling available: $available")
        return super.onPreFling(available)
    }

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
//        Log.e("vsvx13", "onPreScroll available: $available source: $source")
        onChangedOffset(available.x, available.y)
        return super.onPreScroll(available, source)
    }
}
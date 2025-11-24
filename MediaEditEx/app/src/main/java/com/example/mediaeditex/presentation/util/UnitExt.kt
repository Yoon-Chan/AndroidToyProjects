package com.example.mediaeditex.presentation.util

import java.util.Locale

fun Long.toTime(): String {
    val time = this / 1000
    val min = String.format(Locale.getDefault(),"%02d", time / 60)
    val second = String.format(Locale.getDefault(),"%02d", time % 60)
    return "$min:$second"
}
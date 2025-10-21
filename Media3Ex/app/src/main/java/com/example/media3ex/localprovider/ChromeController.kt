package com.example.media3ex.localprovider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class ChromeController {
    var bottomBarVisible by mutableStateOf(true)
        private set

    fun showBottomBar() {
        bottomBarVisible = true
    }

    fun hideBottomBar() {
        bottomBarVisible = false
    }
}

val LocalChromeController = staticCompositionLocalOf<ChromeController> {
    error("No ChromeController provided")
}
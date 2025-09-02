package com.chan.chat_client.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val destinationName: String, val icon: ImageVector) {
    MAIN("홈", Icons.Filled.Home), CHAT_ROOM("채팅룸", Icons.AutoMirrored.Filled.List), MY_PAGE("설정", Icons.Default.Settings)
}
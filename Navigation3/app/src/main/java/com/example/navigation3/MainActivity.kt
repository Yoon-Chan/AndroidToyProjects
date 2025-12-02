package com.example.navigation3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberSupportingPaneSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.navigation3.ui.theme.Navigation3Theme
import kotlinx.serialization.Serializable

data object ChatList

data class ChatDetail(val id: String)

data class Profile(val profileId: String)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation3Theme {
                val backStack = remember { mutableStateListOf<Any>(ChatList) }
                val strategy = rememberListDetailSceneStrategy<Any>()
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    sceneStrategy = strategy,
                    entryProvider = entryProvider {
                        chatListEntry(backStack)
                        chatDetailEntry(backStack)
                        profileEntry()
                    },
                    transitionSpec = {
                        slideInHorizontally { it } togetherWith  slideOutHorizontally { -it }
                    },
                    popTransitionSpec = {
                        slideInHorizontally { -it } togetherWith  slideOutHorizontally { it }
                    },
                    predictivePopTransitionSpec = {
                        slideInHorizontally { -it } togetherWith  slideOutHorizontally { it }
                    }
                )
            }
        }
    }
}

private fun EntryProviderScope<Any>.profileEntry() {
    entry<Profile>(
        metadata = NavDisplay.transitionSpec {
            slideInVertically(animationSpec = tween(1000)) { it } togetherWith ExitTransition.KeepUntilTransitionsFinished
        } + NavDisplay.popTransitionSpec {
            EnterTransition.None togetherWith slideOutVertically(animationSpec = tween(1000)) { it }
        } + NavDisplay.predictivePopTransitionSpec {
            EnterTransition.None togetherWith slideOutVertically(animationSpec = tween(1000)) { it }
        }
    ) { key ->
        ProfileContent(
            profileId = key.profileId
        )
    }
}
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun EntryProviderScope<Any>.chatDetailEntry(backStack: SnapshotStateList<Any>) {
    entry<ChatDetail>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) { key ->
        ChatDetailContent(
            chatId = key.id,
            onProfilePhotoClicked = { profileId ->
                backStack.add(Profile(profileId))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun EntryProviderScope<Any>.chatListEntry(backStack: SnapshotStateList<Any>) {
    entry<ChatList>(
        metadata = ListDetailSceneStrategy.listPane()
    ) {
        ChatListContent(
            onChatClicked = { id ->
                backStack.add(ChatDetail(id))
            }
        )
    }
}

@Composable
fun ChatListContent(
    modifier: Modifier = Modifier,
    onChatClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(100) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = Color.Gray)
                    .clickable {
                        onChatClicked(index.toString())
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${index + 1}")
            }
        }

    }
}

@Composable
fun ChatDetailContent(modifier: Modifier = Modifier, chatId: String, onProfilePhotoClicked: (String) -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .clickable {
                onProfilePhotoClicked(chatId)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chat Detail: $chatId")
    }
}

@Composable
fun ProfileContent(modifier: Modifier = Modifier, profileId : String) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "ProfileContent Content: $profileId")
    }
}

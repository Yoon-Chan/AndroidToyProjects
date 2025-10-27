package com.example.media3ex.presentation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.media3ex.component.TopbarNestedScrollController
import com.example.media3ex.component.YoutubeItem
import com.example.media3ex.component.YoutubeTopbar
import com.example.media3ex.localprovider.LocalChromeController
import com.example.media3ex.navigation.MainEvent
import com.example.media3ex.navigation.MainScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(MainScreen::class, ActivityRetainedComponent::class)
class MainUi : Ui<MainScreen.State> {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content(
        state: MainScreen.State,
        modifier: Modifier
    ) {
        val density = LocalDensity.current
        var offsetY by remember {
            mutableStateOf(0.dp)
        }
        val statusBarDp = StatusBarHeightDp()

        val nestedScroll = remember {
            TopbarNestedScrollController { x, y ->
                val value = with(density) { y.toDp() }
                if(offsetY + value <= ((-56).dp - statusBarDp -  16.dp)) {
                    offsetY = ((-56).dp - statusBarDp -  16.dp)
                } else {
                    offsetY = min( offsetY + value, 0.dp)
                }
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScroll)
            ) {
                items(10) {
                    YoutubeItem(
                        onClick = {
                            state.eventSink(MainEvent.OnDetail(it.toLong()))
                        }
                    )
                }
            }

            YoutubeTopbar(
                modifier = Modifier
                    .offset(x = 0.dp, y = offsetY),
            )
        }

//        AnimatedVisibility(
//            visible = isDetail,
//            enter = slideInVertically { it } ,
//            exit = slideOutVertically { it }
//        ) {
//            DetailUi(DetailScreen.State(0) {})
////            Box(
////                modifier = Modifier
////                    .fillMaxSize()
////                    .background(color = Color.Magenta)
////            )
//        }
    }
}

@Composable
fun StatusBarHeightDp(): Dp {
    val density = LocalDensity.current
    val insets = WindowInsets.statusBars
    val topPx = insets.getTop(density) // px 단위
    return with(density) { topPx.toDp() } // Dp로 변환
}


@Preview
@Composable
private fun MainScreenPreview() {
    val state = MainScreen.State(id = 1) {}
    MainUi().Content(state, modifier = Modifier)
}
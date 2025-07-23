package com.example.viewpagerex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.viewpagerex.ui.theme.ViewPagerExTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewPagerExTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ViewPagerEx(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ViewPagerEx(name: String, modifier: Modifier = Modifier) {
    val firstBackgroundList = listOf(
        Color.Red,
        Color.Blue,
        Color.Cyan,
        Color.Gray,
        Color.Black,
        Color.DarkGray
    )

    val secondBackgroundList = listOf(
        Color.Yellow,
        Color.Magenta,
        Color.LightGray,
        Color.DarkGray,
        Color.Black,
        Color.Cyan,
        Color.Red,
        Color.Blue
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        //첫 번째 UI
        item {
            FirstUi(info = firstBackgroundList)
        }

        //두 번째 UI
        item {
            SecondUi(info = secondBackgroundList)
        }
    }

}

@Composable
fun FirstUi(modifier: Modifier = Modifier, info: List<Color>) {
    val infiniteCount = Int.MAX_VALUE
    val startIndex = (infiniteCount / 2) - ((infiniteCount / 2) % info.size)
    val pagerState = rememberPagerState(
        initialPage = startIndex,
    ) { infiniteCount }
    var isPressed by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(!isPressed) {
        if (!isPressed) {
            while (true) {
                delay(2000L)
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            modifier = Modifier.pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Release -> {
                                isPressed = false
                            }

                            PointerEventType.Press -> {
                                isPressed = true
                            }
                        }
                    }
                }
            },
            state = pagerState,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = info[(it % info.size)])
                    .height(400.dp)
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            text = "${(pagerState.currentPage % info.size) + 1} / ${info.size}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

@Composable
fun SecondUi(modifier: Modifier = Modifier, info: List<Color>) {
    val pagerState = rememberPagerState(
        initialPage = (info.size / 2),
    ) { info.size }
    val rotateDegree = 15F
    val widthWeight = 0.5F
    val pageSpacing = 30.dp
    val configuration = LocalConfiguration.current
    val pageSize = PageSize.Fixed(pageSize = (configuration.screenWidthDp * widthWeight).dp)

    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
        pageSize = pageSize,
        contentPadding = PaddingValues(horizontal = (configuration.screenWidthDp.dp * (1f - widthWeight) / 2)),
        pageSpacing = pageSpacing
    ) { page ->
        Box(
            modifier = Modifier
                .graphicsLayer {
                    val pageOffset = ((pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction
                            ).absoluteValue


                    rotationZ = when {
                        page < pagerState.currentPage -> -rotateDegree
                        page > pagerState.currentPage -> rotateDegree
                        else -> 0F
                    }

                    val distance = (configuration.screenWidthDp.dp / 2) - pageSpacing
                    val height = sin(Math.toRadians(rotateDegree.toDouble())) * distance.toPx()

                    translationY = (height * pageOffset.absoluteValue).toFloat()

                    alpha = lerp(
                        start = 0.8F,
                        stop = 1F,
                        fraction = 1F - pageOffset.absoluteValue.coerceIn(0F, 1F)
                    )
                }
                .width(200.dp)
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(info[(page % info.size)])

        )
    }
}

/*
.graphicsLayer {
                    val pageOffset = ((pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction
                            ).absoluteValue


                    rotationZ = when {
                        page < pagerState.currentPage -> -rotateDegree
                        page > pagerState.currentPage -> rotateDegree
                        else -> 0F
                    }

                    val distance = (configuration.screenWidthDp.dp / 2) - pageSpacing
                    val height = sin(Math.toRadians(rotateDegree.toDouble())) * distance.toPx()

                    translationY = (height * pageOffset.absoluteValue).toFloat()

                    alpha = lerp(
                        start = 0.8F,
                        stop = 1F,
                        fraction = 1F - pageOffset.absoluteValue.coerceIn(0F, 1F)
                    )
                }
*
**/

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ViewPagerExTheme {
        ViewPagerEx("Android")
    }
}
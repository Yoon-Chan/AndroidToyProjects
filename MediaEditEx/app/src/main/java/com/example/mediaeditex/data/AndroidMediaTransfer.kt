package com.example.mediaeditex.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.OverlaySettings
import androidx.media3.common.VideoCompositorSettings
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.CanvasOverlay
import androidx.media3.effect.GlEffect
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.StaticOverlaySettings
import androidx.media3.effect.TextureOverlay
import androidx.media3.transformer.Composition
import androidx.media3.transformer.Composition.HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_MEDIACODEC
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.Effects
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.ProgressHolder
import androidx.media3.transformer.Transformer
import com.example.mediaeditex.domain.MediaTransfer
import com.example.mediaeditex.domain.TransferState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@OptIn(UnstableApi::class)
class AndroidMediaTransfer @Inject constructor(
    private val context: Context,
) : MediaTransfer {

    private val transformer = Transformer.Builder(context)
        .setVideoMimeType(MimeTypes.VIDEO_H264)
        .setUsePlatformDiagnostics(true)
        .build()

    override suspend fun startTransfer(url: String, text: String): Flow<TransferState> =
        channelFlow {
            val path = "${context.cacheDir.absolutePath}/${System.currentTimeMillis()}.mp4"
            val transformerListener: Transformer.Listener =
                object : Transformer.Listener {
                    override fun onCompleted(composition: Composition, result: ExportResult) {
                        Log.e("vsvx13", "onCompleted : $composition ${result.videoEncoderName}")
                        trySend(TransferState.Done(path))
                        transformer.removeListener(this)
                    }

                    override fun onError(
                        composition: Composition, result: ExportResult,
                        exception: ExportException
                    ) {
                        Log.e("vsvx13", "onError : $composition $result $exception")
                        trySend(TransferState.Error(exception.message))
                        transformer.removeListener(this)
                    }
                }
            transformer.addListener(transformerListener)

            val overlayEffect = OverlayEffect(listOf(overlay()))
            val inputMediaItem = MediaItem.fromUri(url.toUri())
            
            val editedMediaItem = EditedMediaItem.Builder(inputMediaItem)
                .setEffects(Effects(listOf(), listOf(overlayEffect)))
                .build()

            transformer.start(editedMediaItem, path)
            val progressHolder = ProgressHolder()
            var progressState = transformer.getProgress(progressHolder)
            while (progressState != Transformer.PROGRESS_STATE_NOT_STARTED) {
                delay(200L)
                progressState = transformer.getProgress(progressHolder)
                if (progressState != Transformer.PROGRESS_STATE_NOT_STARTED) {
                    trySend(TransferState.Progress(progressHolder.progress))
                }
            }
        }

    override suspend fun startTransferMixingMusic(
        mediaUrl: String,
        mediaUrl2: String,
        musicUrl: String
    ): Flow<String> = channelFlow {
        val path = "${context.filesDir.absolutePath}/${System.currentTimeMillis()}.mp4"
        Log.e("vsvx13", "startTransferMixingMusic $path")
        val transformerListener: Transformer.Listener =
            object : Transformer.Listener {
                override fun onCompleted(composition: Composition, result: ExportResult) {
                    Log.e("vsvx13", "onCompleted : videoEncoderName : ${result.videoEncoderName}")
                    trySend(path)
                    transformer.removeListener(this)
                }

                override fun onError(
                    composition: Composition, result: ExportResult,
                    exception: ExportException
                ) {
                    Log.e("vsvx13", "onError : $composition $result $exception")
                    transformer.removeListener(this)
                    throw RuntimeException("인코딩 오류")
                }
            }
        transformer.addListener(transformerListener)

        val overlayEffect = OverlayEffect(listOf(overlay()))
        //비디오 음성 제거
        val video1NoAudio = EditedMediaItem.Builder(MediaItem.fromUri(mediaUrl))
            .setRemoveAudio(true)  // 비디오에 포함된 오디오 트랙 제거
            .build()

        val video2NoAudio = EditedMediaItem.Builder(MediaItem.fromUri(mediaUrl2))
            .setRemoveAudio(true)  // 비디오에 포함된 오디오 트랙 제거
            .build()

        val duration = getVideoDuration(context, mediaUrl.toUri())
        Log.e("vsvx13", "durationUs ${duration}")
        //음악
        val bgmAudio = EditedMediaItem.Builder(
            MediaItem.Builder()
                .setUri(musicUrl)
                .setClippingConfiguration(
                    MediaItem.ClippingConfiguration.Builder()
                        .setStartPositionMs(0L)
                        .setEndPositionMs(duration)
                        .build()
                )
                .build()
        )
            .build()

        val videoSeq = EditedMediaItemSequence.Builder()
            .addItem(video1NoAudio)
            .experimentalSetForceVideoTrack(true)
            .build()

        val videoSeq2 = EditedMediaItemSequence.Builder()
            .addItem(video2NoAudio)
            .experimentalSetForceVideoTrack(true)
            .build()

        //    - 오디오 시퀀스: 배경음악 하나를 "루프"해서 비디오 길이 내내 재생하고 싶다면 isLooping = true
        val audioSeq = EditedMediaItemSequence.Builder()
            .addItem(bgmAudio)
            .setIsLooping(true)
            .build()

        val composition = Composition.Builder(listOf(videoSeq, videoSeq2, audioSeq))
            .setVideoCompositorSettings(compositionSettings())
            .setHdrMode(HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_MEDIACODEC)
            .setEffects(Effects(listOf(), listOf(overlayEffect)))
            .build()

        Log.e("vsvx13", "start transform")
        transformer.start(composition, path)
        val progressHolder = ProgressHolder()
        var progressState = transformer.getProgress(progressHolder)
        while (progressState != Transformer.PROGRESS_STATE_NOT_STARTED) {
            delay(200L)
            progressState = transformer.getProgress(progressHolder)
            Log.e("vsvx13", "progressHolder.progress ${progressHolder.progress}")
        }
    }

    private fun overlay(): TextureOverlay {
        return object : CanvasOverlay(true) {
            override fun onDraw(canvas: Canvas, presentationTimeUs: Long) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.RED }
                if (presentationTimeUs in 1_000_000..15_000_000) {
                    canvas.drawRect(400f, 100f, 2000f, 1500f, paint)
                }
                if (presentationTimeUs > 4_000_000) {
                    canvas.drawText(
                        "이 텍스트는 4초 뒤에 뜹니다.",
                        300f,
                        600f,
                        Paint().apply {
                            this.textSize = 24f
                            this.color = Color.BLUE
                            this.style = Paint.Style.STROKE
                        }
                    )
                }
            }
        }
    }

    private fun compositionSettings() = object : VideoCompositorSettings {
        // 입력 사이즈 목록을 보고 출력 캔버스 크기 결정
        // 가로 = (왼쪽 너비 + 오른쪽 너비), 세로 = 두 입력 중 최대 높이
        override fun getOutputSize(inputSizes: List<Size>): Size {
            // 안전 처리: 입력 2개 보장 가정
            val left  = inputSizes.getOrNull(0) ?: Size(1280, 720)
            val right = inputSizes.getOrNull(1) ?: left
            val width  = left.width + right.width
            val height = maxOf(left.height, right.height)
            return Size(width, height)
        }

        // 각 입력을 어디에/어떻게 올릴지 정의
        // 좌표계는 배경 프레임을 (-1,-1)~(1,1)로 보는 개념이라
        // 앵커를 -0.5/0.5 등으로 주면 절반 폭/높이에 해당하는 영역에 딱 맞습니다.
        override fun getOverlaySettings(inputId: Int, presentationTimeUs: Long) =
            when (inputId) {
                0 -> { // 왼쪽: A
                    StaticOverlaySettings.Builder()
                        .setScale(1f, 1f)                 // 출력 가로의 절반 폭 차지(세로는 꽉)
                        .setOverlayFrameAnchor(0f, 0f)      // 오버레이 기준점 (중앙)
                        .setBackgroundFrameAnchor(-0.5f, 0f) // 배경의 왼쪽 중앙에 정렬
                        .build()
                }
                1 -> { // 오른쪽: B
                    StaticOverlaySettings.Builder()
                        .setScale(1f, 1f)
                        .setOverlayFrameAnchor(0f, 0f)
                        .setBackgroundFrameAnchor(0.5f, 0f)  // 배경의 오른쪽 중앙에 정렬
                        .build()
                }
                else -> StaticOverlaySettings.Builder().build()
            }
    }

    private fun getVideoDuration(context: Context, uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationStr?.toLong() ?: 0L // ms 단위
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        } finally {
            retriever.release()
        }
    }
}
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
import androidx.media3.common.OverlaySettings
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.CanvasOverlay
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.TextureOverlay
import androidx.media3.transformer.Composition
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
): MediaTransfer {

    private val transformer = Transformer.Builder(context)
        .build()

    override suspend fun startTransfer(url: String, text: String): Flow<TransferState> = channelFlow {
        val path = "${context.cacheDir.absolutePath}/${System.currentTimeMillis()}.mp4"
        val transformerListener: Transformer.Listener =
            object : Transformer.Listener {
                override fun onCompleted(composition: Composition, result: ExportResult) {
                    Log.e("vsvx13", "onCompleted : $composition ${result.videoEncoderName}")
                    trySend(TransferState.Done(path))
                    transformer.removeListener(this)
                }

                override fun onError(composition: Composition, result: ExportResult,
                                     exception: ExportException) {
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
            if(progressState != Transformer.PROGRESS_STATE_NOT_STARTED) {
                trySend(TransferState.Progress(progressHolder.progress))
            }
        }
    }

    override suspend fun startTransferMixingMusic(
        mediaUrl: String,
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

                override fun onError(composition: Composition, result: ExportResult,
                                     exception: ExportException) {
                    Log.e("vsvx13", "onError : $composition $result $exception")
                    transformer.removeListener(this)
                    throw RuntimeException("인코딩 오류")
                }
            }
        transformer.addListener(transformerListener)

        //비디오 음성 제거
        val videoNoAudio = EditedMediaItem.Builder(MediaItem.fromUri(mediaUrl))
            .setRemoveAudio(true)  // 비디오에 포함된 오디오 트랙 제거
            .build()

        val duration = getVideoDuration(context,mediaUrl.toUri())
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
            .setDurationUs(duration)
            .build()

        val videoSeq = EditedMediaItemSequence.Builder()
            .addItem(videoNoAudio)
            .setIsLooping(true)
            .build()

        //    - 오디오 시퀀스: 배경음악 하나를 "루프"해서 비디오 길이 내내 재생하고 싶다면 isLooping = true
        val audioSeq = EditedMediaItemSequence.Builder()
            .addItem(bgmAudio)
            .build()

        val composition = Composition.Builder(videoSeq, *arrayOf(audioSeq))
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
                val paint = Paint().apply {
                    this.color = Color.RED
                }
                if(presentationTimeUs in 1_000_000 .. 3_000_000) {
                    canvas.drawRect(100f, 100f, 500f, 500f, paint)
                }
                if(presentationTimeUs > 4_000_000) {
                    canvas.drawText(
                        "이 텍스트는 4초 뒤에 뜹니다.",
                        300f,
                        600f,
                        Paint().apply {
                            this.textSize = 24f
                            this.color = Color.BLUE
                            this.style=Paint.Style.STROKE
                        }
                    )
                }
            }
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
package com.example.mediaeditex.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import com.example.mediaeditex.data.util.toByteArray
import com.example.mediaeditex.domain.RecordRepository
import com.example.mediaeditex.domain.Video
import com.example.mediaeditex.domain.VideoInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val context: Context
) : RecordRepository {
    override fun getVideoInfo(url: String): Flow<VideoInfo> = flow {
        emit(VideoInfo.Loading(url))
        delay(1000L)
        emit(VideoInfo.Done(extractVideoFrameAt(context, uri = url, 0L)))
    }

    /**
     * [timeMs] 시점의 프레임을 Bitmap으로 추출
     * timeMs = 0 이면 0초 프레임
     */
    fun extractVideoFrameAt(
        context: Context,
        uri: String,
        timeMs: Long
    ): Video {
        val retriever = MediaMetadataRetriever()
        return try {
            // content://, file:// 모두 지원
            val file = File(context.cacheDir, uri)
            retriever.setDataSource(file.absolutePath)
            Log.e("vsvx13", "retriever ${retriever.frameAtTime} $retriever")

            // MediaMetadataRetriever는 us(마이크로초) 단위 사용
            val timeUs = timeMs * 1000

            // 두 번째 인자는 어떤 프레임을 가져올지 옵션
            // - OPTION_CLOSEST: 지정 시간과 가장 가까운 프레임
            // - OPTION_CLOSEST_SYNC: 가장 가까운 키프레임(보통 썸네일용)
            // - OPTION_NEXT_SYNC: 지정 시간 이후 첫 키프레임
            val bitmap = retriever.getFrameAtTime(timeUs)
            Log.e(
                "vsvx13",
                "uri $uri bitmap $bitmap, duration ${
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                }"
            )
            Log.e(
                "vsvx13",
                "path=${file.absolutePath}, exists=${file.exists()}, length=${file.length()}"
            )
            if (bitmap == null) throw RuntimeException("bitmap is Null")

            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                    ?: throw RuntimeException("duration is Null")
            Video(
                url = uri,
                duration = duration,
                thumbnail = bitmap.toByteArray()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: 오류 관련 처리 IllegalArgumentException, SecurityException
            throw e
        } finally {
            retriever.release()
        }
    }
}
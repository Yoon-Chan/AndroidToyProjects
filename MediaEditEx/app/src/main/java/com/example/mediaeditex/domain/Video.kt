package com.example.mediaeditex.domain

data class Video(
    val url: String,
    val duration: Long,
    val thumbnail: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Video

        if (duration != other.duration) return false
        if (url != other.url) return false
        if (!thumbnail.contentEquals(other.thumbnail)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = duration.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + thumbnail.contentHashCode()
        return result
    }
}

package com.example.mediaeditex.domain

sealed interface DrawType {

    data class Rect(
        val x: Float,
        val y: Float,
        val size: Int,
        val bgColor: Long,
        val start: Long,
        val end: Long,
    ) : DrawType

    data class Text(
        val text: String,
        val size: Float,
        val color: Int,
        val start: Long,
        val end: Long,
    ): DrawType
}
package com.example.mediaeditex.presentation.model

interface MainEvent {
    data class OnClickTransfer(val url: String, val text: String): MainEvent
    data class CreateRect(val x: Float, val y: Float, val size: Int = 300): MainEvent
    data class CreateText(val text: String, val x: Float, val y: Float): MainEvent
}
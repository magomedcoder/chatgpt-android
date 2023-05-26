package ru.magomedcoder.chatgpt.domain.model

data class VolumeState(
    val touchDown: Boolean = false,
    val touchUp: Boolean = false
)
package ru.magomedcoder.chatgpt.domain.model

data class VoiceToTextParserState(
    val isSpeaking: Boolean = false,
    val spokenText: String = "",
    val error: String? = null
)